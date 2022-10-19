package ru.clevertec.ecl.util.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.model.GiftCertificate;

@Mapper(uses = TagDtoMapper.class)
public interface GiftCertificateDtoMapper {

    GiftCertificateDto mapToDto(GiftCertificate certificate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    GiftCertificate mapCreationDtoToEntity(GiftCertificateCreateDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateEntityIgnoringTags(@MappingTarget GiftCertificate target, GiftCertificateUpdateDto updateDto);
}
