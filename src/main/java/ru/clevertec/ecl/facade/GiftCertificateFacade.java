package ru.clevertec.ecl.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;

public interface GiftCertificateFacade {

    Page<GiftCertificateDto> findAllByFilter(Pageable pageable, GiftCertificateFilterDto filterDto);

    GiftCertificateDto findById(long id);

    GiftCertificateDto create(GiftCertificateCreateDto createDto);

    GiftCertificateDto updateById(long id, GiftCertificateUpdateDto updateDto);

    void deleteById(long id);
}
