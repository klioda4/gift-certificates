package ru.clevertec.ecl.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaSpecificationExecutor;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateRepository extends EntityGraphJpaRepository<GiftCertificate, Long>,
    EntityGraphJpaSpecificationExecutor<GiftCertificate> {

}
