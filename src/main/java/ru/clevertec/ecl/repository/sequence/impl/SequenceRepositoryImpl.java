package ru.clevertec.ecl.repository.sequence.impl;

import com.google.common.base.CaseFormat;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.repository.sequence.SequenceRepository;

@RequiredArgsConstructor
@Repository
public class SequenceRepositoryImpl implements SequenceRepository {

    private static final String NEXT_SEQUENCE_QUERY_PATTERN = "SELECT nextval('%s_id_seq')";
    private static final String CURRENT_SEQUENCE_QUERY_PATTERN = "SELECT last_value FROM %s_id_seq";
    private static final String SET_SEQUENCE_VALUE_QUERY_PATTERN = "SELECT setval('%s_id_seq', %d)";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long getNextSequence(String entityName) {
        String tableName = getTableName(entityName);
        Query sequenceQuery = entityManager.createNativeQuery(String.format(NEXT_SEQUENCE_QUERY_PATTERN, tableName));
        BigInteger nextSequenceValue = (BigInteger) sequenceQuery.getSingleResult();
        return nextSequenceValue.longValue();
    }

    @Override
    public long getCurrentSequence(String entityName) {
        String tableName = getTableName(entityName);
        Query sequenceQuery = entityManager.createNativeQuery(String.format(CURRENT_SEQUENCE_QUERY_PATTERN, tableName));
        BigInteger currentSequenceValue = (BigInteger) sequenceQuery.getSingleResult();
        return currentSequenceValue.longValue();
    }

    @Override
    public void setCurrentSequence(String entityName, long newValue) {
        String tableName = getTableName(entityName);
        Query setSequenceQuery = entityManager.createNativeQuery(
            String.format(SET_SEQUENCE_VALUE_QUERY_PATTERN, tableName, newValue));
        setSequenceQuery.getSingleResult();
    }

    private String getTableName(String entityName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityName);
    }
}
