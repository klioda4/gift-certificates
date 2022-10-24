package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificate;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMappedFromCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMergedWithUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateUpdateDto;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;

class GiftCertificateDtoMapperTest {

    private final GiftCertificateDtoMapper mapper = Mappers.getMapper(GiftCertificateDtoMapper.class);

    @Test
    void givenGiftCertificate_whenMapToDto_thenReturnCorrectDto() {
        GiftCertificate givenCertificate = getGiftCertificate();

        GiftCertificateDto actual = mapper.mapToDto(givenCertificate);

        GiftCertificateDto expected = getGiftCertificateDto();
        assertEquals(expected, actual);
    }

    @Test
    void givenCreateDto_whenMapCreationDtoToEntity_thenReturnCorrectGiftCertificate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();

        GiftCertificate actual = mapper.mapCreationDtoToEntity(givenCreateDto);

        GiftCertificate expected = getGiftCertificateMappedFromCreateDto();
        expected.setCreateDate(actual.getCreateDate());
        assertEquals(expected, actual);
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenUpdateOnlySpecifiedFields() {
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();

        mapper.updateEntityIgnoringTags(giftCertificateToUpdate, givenUpdateDto);

        GiftCertificate expected = getGiftCertificateMergedWithUpdateDto();
        assertEquals(expected, giftCertificateToUpdate);
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenNotUpdateTagList() {
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();

        mapper.updateEntityIgnoringTags(giftCertificateToUpdate, givenUpdateDto);

        assertEquals(Collections.emptyList(), giftCertificateToUpdate.getTags(), "Tags need to stay unchanged");
    }
}