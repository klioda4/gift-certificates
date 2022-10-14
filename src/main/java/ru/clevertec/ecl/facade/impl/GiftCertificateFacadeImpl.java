package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
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
    public Page<GiftCertificateDto> findByTagName(String tagName, Pageable pageable) {
        return certificateService.findByTagName(tagName, pageable)
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
    public void deleteById(long id) {
        certificateService.deleteById(id);
    }
}
