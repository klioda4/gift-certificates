package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateService {

    GiftCertificate findById(long id) throws ObjectNotFoundException;

    GiftCertificate create(GiftCertificateCreateDto createDto);

    GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto) throws ObjectNotFoundException;

    void deleteById(long id) throws ObjectNotFoundException;

    Page<GiftCertificate> findAllByFilters(Pageable pageable, GiftCertificateFilterDto filterDto);
}
