package ru.clevertec.ecl.util.mapping.ext;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.util.mapping.DtoMapper;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = TagDtoMapper.class)
public interface GiftCertificateMapper extends
    DtoMapper<GiftCertificate, GiftCertificateDto, GiftCertificateCreateDto> {

    @Override
    GiftCertificateDto mapToDto(GiftCertificate dto);

    @Override
    GiftCertificate mapToEntity(GiftCertificateDto giftCertificateDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate mapCreateDtoToEntity(GiftCertificateCreateDto giftCertificateCreateDto);
}
