package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagDto;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagMappedFromPutDto;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagPutDto;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.request.TagPutDto;
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
    void givenTagPutDto_whenMapPutDtoToEntity_thenReturnExpectedTag() {
        TagPutDto givenPutDto = getTagPutDto();

        Tag actualTag = tagMapper.mapPutDtoToEntity(givenPutDto);

        Tag expectedTag = getTagMappedFromPutDto();
        assertEquals(expectedTag, actualTag);
    }
}
