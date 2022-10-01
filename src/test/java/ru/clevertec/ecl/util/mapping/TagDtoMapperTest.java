package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;

class TagDtoMapperTest {

    private final TagDtoMapper mapper = Mappers.getMapper(TagDtoMapper.class);

    @Test
    void givenTag_whenMapToDto_thenReturnCorrectDto() {
        Tag givenTag = new Tag(1L, "tag1");

        TagDto actual = mapper.mapToDto(givenTag);

        TagDto expected = new TagDto(1L, "tag1");
        assertEquals(expected, actual);
    }

    @Test
    void givenTagPutDto_whenMapPutDtoToEntity_thenReturnCorrectTag() {
        TagPutDto givenPutDto = new TagPutDto("tag1");

        Tag actual = mapper.mapPutDtoToEntity(givenPutDto);

        Tag expected = new Tag(0, "tag1");
        assertEquals(expected, actual);
    }
}