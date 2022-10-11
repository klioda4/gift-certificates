package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateService {

    GiftCertificate findById(long id);

    GiftCertificate create(GiftCertificateCreateDto createDto);

    GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto);

    void deleteById(long id);

    Page<GiftCertificate> findAllByFilters(Pageable pageable, GiftCertificateFilterDto filterDto);
}
