package ru.clevertec.ecl.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.facade.GiftCertificateFacade;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@RequiredArgsConstructor
@Component
public class GiftCertificateFacadeImpl implements GiftCertificateFacade {

    private final GiftCertificateService certificateService;
    private final GiftCertificateDtoMapper certificateMapper;

    @Override
    public Page<GiftCertificateDto> findAllByNameAndDescription(String nameSample, String descriptionSample,
                                                                Pageable pageable) {
        return certificateService.findAllByNameAndDescription(nameSample, descriptionSample, pageable)
            .map(certificateMapper::mapToDto);
    }

    @Override
    public Page<GiftCertificateDto> findAllByTagNames(List<String> tagNames, Pageable pageable) {
        return certificateService.findAllByTagNames(tagNames, pageable)
            .map(certificateMapper::mapToDto);
    }

    @Override
    public GiftCertificateDto findById(long id) {
        return certificateMapper.mapToDto(
            certificateService.findById(id));
    }

    @Override
    public GiftCertificateDto create(GiftCertificateCreateDto createDto) {
        return certificateMapper.mapToDto(
            certificateService.create(createDto));
    }

    @Override
    public GiftCertificateDto updateById(long id, GiftCertificateUpdateDto updateDto) {
        return certificateMapper.mapToDto(
            certificateService.updateById(id, updateDto));
    }

    @Override
    public void updatePriceById(long id, GiftCertificatePriceUpdateDto priceUpdateDto) {
        certificateService.updatePriceById(id, priceUpdateDto);
    }

    @Override
    public void deleteById(long id) {
        certificateService.deleteById(id);
    }
}
