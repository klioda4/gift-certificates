package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TagDtoMapper {

    TagDto mapToDto(Tag dto);

    @Mapping(target = "id", ignore = true)
    Tag mapPutDtoToEntity(TagPutDto putDto);
}
