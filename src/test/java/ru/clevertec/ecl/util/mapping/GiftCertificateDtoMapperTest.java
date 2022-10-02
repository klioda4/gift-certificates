package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificate;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMappedFromCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMergedWithUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateUpdateDto;

import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier;

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
    void givenCreateDto_whenMapCreationDtoToEntity_thenSetCreateDateAndUpdateDateInReturningCertificate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();

        GiftCertificate result = mapper.mapCreationDtoToEntity(givenCreateDto);

        assertNotNull(result.getCreateDate(), "Creation date needs to be set");
        assertNotNull(result.getLastUpdateDate(), "Last update date needs to be set");
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenUpdateOnlySpecifiedFields() {
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();

        mapper.updateEntityIgnoringTags(givenUpdateDto, giftCertificateToUpdate);

        GiftCertificate expected = getGiftCertificateMergedWithUpdateDto();
        assertEquals(expected, giftCertificateToUpdate);
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenNotUpdateTagList() {
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();

        mapper.updateEntityIgnoringTags(givenUpdateDto, giftCertificateToUpdate);

        assertEquals(Collections.emptyList(), giftCertificateToUpdate.getTags(), "Tags need to stay unchanged");
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenSetLastUpdateDate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();
        LocalDateTime defaultDate = GiftCertificateTestDataSupplier.DEFAULT_DATE;

        GiftCertificate result = mapper.mapCreationDtoToEntity(givenCreateDto);

        assertNotEquals(defaultDate, result.getLastUpdateDate(), "Last update date needs to be set");
    }
}