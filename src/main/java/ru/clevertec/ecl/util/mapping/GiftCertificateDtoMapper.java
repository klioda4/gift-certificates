package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreationDto;
import ru.clevertec.ecl.model.GiftCertificate;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = TagDtoMapper.class)
public interface GiftCertificateDtoMapper {

    GiftCertificateDto mapToDto(GiftCertificate dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate mapCreationDtoToEntity(GiftCertificateCreationDto giftCertificateCreationDto);
}
