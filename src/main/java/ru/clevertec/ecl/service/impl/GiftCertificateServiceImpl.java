package ru.clevertec.ecl.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.util.mapping.DtoMapper;

@Service
public class GiftCertificateServiceImpl extends CrudServiceImpl<GiftCertificate, GiftCertificateDto,
    GiftCertificateCreateDto, Long> {

    public GiftCertificateServiceImpl(
        JpaRepository<GiftCertificate, Long> repository,
        DtoMapper<GiftCertificate, GiftCertificateDto, GiftCertificateCreateDto> mapper) {

        super(repository, mapper);
    }
}
