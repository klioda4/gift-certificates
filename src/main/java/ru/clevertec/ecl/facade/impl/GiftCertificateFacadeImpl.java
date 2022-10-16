package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.facade.GiftCertificateFacade;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@RequiredArgsConstructor
@Component
public class GiftCertificateFacadeImpl implements GiftCertificateFacade {

    private final GiftCertificateService service;
    private final GiftCertificateDtoMapper mapper;

    @Override
    public Page<GiftCertificateDto> findAllByFilter(Pageable pageable, GiftCertificateFilterDto filterDto) {
        return service.findAllByFilters(pageable, filterDto)
            .map(mapper::mapToDto);
    }

    @Override
    public GiftCertificateDto findById(long id) {
        return mapper.mapToDto(
            service.findById(id));
    }

    @Override
    public GiftCertificateDto create(GiftCertificateCreateDto createDto) {
        return mapper.mapToDto(
            service.create(createDto));
    }

    @Override
    public GiftCertificateDto updateById(long id, GiftCertificateUpdateDto updateDto) {
        return mapper.mapToDto(
            service.updateById(id, updateDto));
    }

    @Override
    public void deleteById(long id) {
        service.deleteById(id);
    }
}
