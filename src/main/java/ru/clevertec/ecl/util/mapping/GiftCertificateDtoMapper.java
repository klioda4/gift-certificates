package ru.clevertec.ecl.util.mapping;

import java.time.LocalDateTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.GiftCertificate_;

@Mapper(
    componentModel = ComponentModel.SPRING,
    uses = TagDtoMapper.class,
    imports = LocalDateTime.class,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GiftCertificateDtoMapper {

    GiftCertificateDto mapToDto(GiftCertificate dto);

    @Mapping(target = GiftCertificate_.CREATE_DATE, expression = "java(LocalDateTime.now())")
    @Mapping(target = GiftCertificate_.LAST_UPDATE_DATE, expression = "java(LocalDateTime.now())")
    GiftCertificate mapCreationDtoToEntity(GiftCertificateCreateDto giftCertificateCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = GiftCertificate_.LAST_UPDATE_DATE, expression = "java(LocalDateTime.now())")
    void updateEntityIgnoringTags(GiftCertificateUpdateDto updateDto, @MappingTarget GiftCertificate targetEntity);
}
