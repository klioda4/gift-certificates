package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTagDto;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTagMappedFromPutDto;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTagPutDto;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;

class TagDtoMapperTest {

    private final TagDtoMapper mapper = Mappers.getMapper(TagDtoMapper.class);

    @Test
    void givenTag_whenMapToDto_thenReturnCorrectDto() {
        Tag givenTag = getTag();

        TagDto actual = mapper.mapToDto(givenTag);

        TagDto expected = getTagDto();
        assertEquals(expected, actual);
    }

    @Test
    void givenTagPutDto_whenMapPutDtoToEntity_thenReturnCorrectTag() {
        TagPutDto givenPutDto = getTagPutDto();

        Tag actual = mapper.mapPutDtoToEntity(givenPutDto);

        Tag expected = getTagMappedFromPutDto();
        assertEquals(expected, actual);
    }
}