package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagCreateDto;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagDto;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.model.Tag;

class TagDtoMapperTest {

    private final TagDtoMapper tagMapper = Mappers.getMapper(TagDtoMapper.class);

    @Test
    void givenTag_whenMapToDto_thenReturnExpectedDto() {
        Tag givenTag = getTag();

        TagDto actualTag = tagMapper.mapToDto(givenTag);

        TagDto expectedTag = getTagDto();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenTagDto_whenMapToTag_thenReturnExpectedTag() {
        TagCreateDto givenTagCreateDto = getTagCreateDto();

        Tag actualTag = tagMapper.mapToTag(givenTagCreateDto);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }
}
