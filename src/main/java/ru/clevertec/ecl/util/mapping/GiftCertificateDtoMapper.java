package ru.clevertec.ecl.util.mapping;

import java.time.LocalDateTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = TagDtoMapper.class,
    imports = LocalDateTime.class)
public interface GiftCertificateDtoMapper {

    GiftCertificateDto mapToDto(GiftCertificate dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate mapCreationDtoToEntity(GiftCertificateCreateDto giftCertificateCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    void updateEntityIgnoringTags(@MappingTarget GiftCertificate target, GiftCertificateUpdateDto updateDto);
}
