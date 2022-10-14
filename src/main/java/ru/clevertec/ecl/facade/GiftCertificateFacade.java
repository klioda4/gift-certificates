package ru.clevertec.ecl.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;

public interface GiftCertificateFacade {

    Page<GiftCertificateDto> findAllByNameAndDescription(String nameSample, String descriptionSample,
                                                         Pageable pageable);

    Page<GiftCertificateDto> findByTagName(String tagName, Pageable pageable);

    GiftCertificateDto findById(long id);

    GiftCertificateDto create(GiftCertificateCreateDto createDto);

    GiftCertificateDto updateById(long id, GiftCertificateUpdateDto updateDto);

    void deleteById(long id);
}
