package ru.clevertec.ecl.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;

public interface GiftCertificateService {

    Page<GiftCertificate> findAllByNameAndDescription(String nameSample, String descriptionSample, Pageable pageable);

    Page<GiftCertificate> findAllByTagNames(List<String> tagNames, Pageable pageable);

    GiftCertificate findById(long id);

    GiftCertificate findByIdLazy(long id);

    GiftCertificate create(GiftCertificateCreateDto createDto);

    GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto);

    void updatePriceById(long id, GiftCertificatePriceUpdateDto priceUpdateDto);

    void deleteById(long id);
}
