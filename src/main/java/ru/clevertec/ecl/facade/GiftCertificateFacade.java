package ru.clevertec.ecl.facade;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;

public interface GiftCertificateFacade {

    Page<GiftCertificateDto> findAllByNameAndDescription(String nameSample, String descriptionSample,
                                                         Pageable pageable);

    Page<GiftCertificateDto> findAllByTagNames(List<String> tagNames, Pageable pageable);

    GiftCertificateDto findById(long id);

    GiftCertificateDto create(GiftCertificateCreateDto createDto);

    GiftCertificateDto updateById(long id, GiftCertificateUpdateDto updateDto);

    void updatePriceById(long id, GiftCertificatePriceUpdateDto priceUpdateDto);

    void deleteById(long id);
}
