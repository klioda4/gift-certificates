package ru.clevertec.ecl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateRepository extends EntityGraphJpaRepository<GiftCertificate, Long> {

    @Query(value = "       select gc"
                       + " from GiftCertificate gc"
                       + " join fetch gc.tags"
                       + " where gc.id in (select gc.id"
                       + "                 from GiftCertificate gc"
                       + "                 join gc.tags t"
                       + "                 where t.name = :tagName)",
           countQuery = "       select count(gc.id)"
                            + " from GiftCertificate gc"
                            + " join gc.tags t"
                            + " where t.name = :tagName")
    Page<GiftCertificate> findAllByTagName(String tagName, Pageable pageable);

    @Query(value = "       select gc"
                       + " from GiftCertificate gc"
                       + " join fetch gc.tags"
                       + " where gc.id in (select gc.id"
                       + "                 from GiftCertificate gc"
                       + "                 join gc.tags t"
                       + "                 where t.name in :tagNames"
                       + "                 group by gc.id"
                       + "                 having count(gc.id) = :tagNamesSize)",
           countQuery = "       select count(gc.id)"
                            + " from GiftCertificate gc"
                            + " join gc.tags t"
                            + " where t.name in :tagNames"
                            + " group by gc.id"
                            + " having count(gc.id) = :tagNamesSize")
    Page<GiftCertificate> findAllByAllTagNames(List<String> tagNames, long tagNamesSize, Pageable pageable);

    default Page<GiftCertificate> findAllByAllTagNames(List<String> tagNames, Pageable pageable) {
        return findAllByAllTagNames(tagNames, tagNames.size(), pageable);
    }
}
