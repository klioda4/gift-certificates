package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.Named;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.dto.response.TagOfUserDto;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.projection.TagOfUser;

@Mapper(componentModel = ComponentModel.SPRING)
public interface TagDtoMapper {

    TagDto mapToDto(Tag tag);

    @Mapping(target = "id", ignore = true)
    Tag mapPutDtoToEntity(TagPutDto putDto);

    @Mapping(target = "tag", source = "tagOfUser", qualifiedByName = "createTagDto")
    TagOfUserDto mapToDto(TagOfUser tagOfUser);

    @Named("createTagDto")
    static TagDto createTagDto(TagOfUser tagOfUser) {
        return new TagDto(tagOfUser.getTagId(), tagOfUser.getTagName());
    }
}
