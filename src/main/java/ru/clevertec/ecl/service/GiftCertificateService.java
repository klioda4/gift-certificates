package ru.clevertec.ecl.service;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreationDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateService {

    GiftCertificate findById(long id) throws ObjectNotFoundException;

    GiftCertificate create(GiftCertificateCreationDto createDto);

    GiftCertificate updateById(long id, Map<String, Object> newFieldValues) throws ObjectNotFoundException;

    void deleteById(long id) throws ObjectNotFoundException;

    Page<GiftCertificate> findByFilters(Pageable pageable, GiftCertificateFilterDto filterDto);
}
