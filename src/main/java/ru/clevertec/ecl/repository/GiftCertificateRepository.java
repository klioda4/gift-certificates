package ru.clevertec.ecl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.entityGraph.EntityGraphNames;

public interface GiftCertificateRepository extends EntityGraphJpaRepository<GiftCertificate, Long>,
    EntityGraphJpaSpecificationExecutor<GiftCertificate> {

    @Query("select gc"
        + " from GiftCertificate gc"
        + " where gc.id in (select gc.id"
        + "                 from GiftCertificate gc"
        + "                 join gc.tags t"
        + "                 where t.name = :tagName)")
    @EntityGraph(EntityGraphNames.CERTIFICATE_WITH_TAGS)
    Page<GiftCertificate> findByTagName(String tagName, Pageable pageable);
}
