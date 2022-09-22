package ru.clevertec.ecl.util.mapping.ext;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.util.mapping.DtoMapper;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TagDtoMapper extends DtoMapper<Tag, TagDto, TagCreateDto> {

    @Override
    @Mapping(target = "giftCertificates", ignore = true)
    Tag mapToEntity(TagDto tagDto);

    @Override
    TagDto mapToDto(Tag dto);

    @Override
    @Mapping(target = "giftCertificates", ignore = true)
    @Mapping(target = "id", ignore = true)
    Tag mapCreateDtoToEntity(TagCreateDto tagCreateDto);
}
