package ru.clevertec.ecl.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.util.commitLog.CommitLogRequestSender;
import ru.clevertec.ecl.dto.request.CommitLogStatusUpdateDto;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.SequenceConflictException;
import ru.clevertec.ecl.model.CommitLog;
import ru.clevertec.ecl.model.CommitLog_;
import ru.clevertec.ecl.repository.CommitLogRepository;
import ru.clevertec.ecl.repository.sequence.SequenceRepository;
import ru.clevertec.ecl.service.CommitLogService;
import ru.clevertec.ecl.util.enums.CommitLogStatus;
import ru.clevertec.ecl.util.mapping.CommitLogDtoMapper;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class CommitLogServiceImpl implements CommitLogService {

    private static final String COMMIT_LOG_ENTITY_NAME = InterceptorConstants.COMMIT_LOG_ENTITY_NAME;

    private final CommitLogRepository commitLogRepository;
    private final SequenceRepository sequenceRepository;
    private final CommitLogRequestSender commitLogRequestSender;
    private final CommitLogDtoMapper commitLogMapper;

    @Override
    public CommitLogDto findById(long id) {
        return commitLogMapper.mapToDto(
            findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public CommitLogDto create(CommitLogDto commitLogDto, boolean updateSequences) {
        if (HttpMethod.POST.matches(commitLogDto.getHttpMethod())) {
            return saveCreationCommitLog(commitLogDto, updateSequences);
        } else {
            return saveModifyingCommitLog(commitLogDto, updateSequences);
        }
    }

    @Override
    @Transactional
    public void apply(long id) {
        CommitLog commitLog = findByIdOrThrow(id);
        if ((commitLog.getStatus() == CommitLogStatus.APPLIED) || (commitLog.getStatus() == CommitLogStatus.IGNORED)) {
            return;
        }
        CommitLogDto commitLogDto = commitLogMapper.mapToDto(commitLog);
        commitLogRequestSender.applyCommitLog(commitLogDto);
        commitLog.setStatus(CommitLogStatus.APPLIED);
        commitLogRepository.save(commitLog);
    }

    @Override
    @Transactional
    public void decline(long id) {
        commitLogRepository.findById(id)
            .map(commitLog -> {
                commitLog.setStatus(CommitLogStatus.DECLINED);
                return commitLogRepository.save(commitLog);
            })
            .orElseGet(() -> {
                log.info("Current node doesn't have commit log with id={}", id);
                return null;
            });
    }

    @Override
    @Transactional
    public void updateStatus(long id, CommitLogStatusUpdateDto statusUpdateDto) {
        CommitLog commitLog = findByIdOrThrow(id);
        commitLog.setStatus(statusUpdateDto.getStatus());
        commitLogRepository.save(commitLog);
    }

    @Override
    public SequenceDto getCurrentSequence(String entityName) {
        long currentSequenceValue = sequenceRepository.getCurrentSequence(entityName);
        return new SequenceDto(currentSequenceValue);
    }

    @Override
    @Transactional
    public SequenceDto getNextSequence(String entityName) {
        long nextSequenceValue = sequenceRepository.getNextSequence(entityName);
        return new SequenceDto(nextSequenceValue);
    }

    private CommitLog findByIdOrThrow(long id) {
        return commitLogRepository.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException(COMMIT_LOG_ENTITY_NAME, CommitLog_.ID, id));
    }

    private CommitLogDto saveCreationCommitLog(CommitLogDto commitLogDto, boolean updateSequences) {
        if (updateSequences) {
            checkAndUpdateCommitLogSequence(commitLogDto);
            checkAndUpdateEntityLogSequence(commitLogDto);
        }
        return saveCommitLogDto(commitLogDto);
    }

    private CommitLogDto saveModifyingCommitLog(CommitLogDto commitLogDto, boolean updateSequences) {
        if (updateSequences) {
            checkAndUpdateCommitLogSequence(commitLogDto);
        }
        return saveCommitLogDto(commitLogDto);
    }

    private void checkAndUpdateCommitLogSequence(CommitLogDto commitLogDto) {
        long currentCommitLogSequence = sequenceRepository.getCurrentSequence(COMMIT_LOG_ENTITY_NAME);
        checkSequenceActuality(currentCommitLogSequence, commitLogDto.getId(), COMMIT_LOG_ENTITY_NAME);
        sequenceRepository.setCurrentSequence(COMMIT_LOG_ENTITY_NAME, commitLogDto.getId());
    }

    private void checkAndUpdateEntityLogSequence(CommitLogDto commitLogDto) {
        if (commitLogDto.getStatus() == CommitLogStatus.IGNORED) {
            return;
        }
        long currentEntitySequence = sequenceRepository.getCurrentSequence(commitLogDto.getEntityName());
        checkSequenceActuality(currentEntitySequence, commitLogDto.getEntityId(), commitLogDto.getEntityName());
        sequenceRepository.setCurrentSequence(commitLogDto.getEntityName(), commitLogDto.getEntityId());
    }

    private void checkSequenceActuality(long currentSequence, long entityId, String entityName) {
        if (currentSequence >= entityId) {
            log.info("Conflict: {} id={} already taken", entityName, entityId);
            throw new SequenceConflictException(entityName, currentSequence, "<" + entityId);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private CommitLogDto saveCommitLogDto(CommitLogDto commitLogDto) {
        return Optional.of(commitLogDto)
                   .map(commitLogMapper::mapToCommitLog)
                   .map(commitLogRepository::save)
                   .map(commitLogMapper::mapToDto)
                   .get();
    }
}
