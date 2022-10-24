package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificate;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateMappedFromCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateMergedWithUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateUpdateDto;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.model.GiftCertificate;

class GiftCertificateDtoMapperTest {

    private final GiftCertificateDtoMapper certificateMapper = Mappers.getMapper(GiftCertificateDtoMapper.class);

    @Test
    void givenGiftCertificate_whenMapToDto_thenReturnExpectedDto() {
        GiftCertificate givenCertificate = getGiftCertificate();

        GiftCertificateDto actualCertificate = certificateMapper.mapToDto(givenCertificate);

        GiftCertificateDto expectedCertificate = getGiftCertificateDto();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenCreateDto_whenMapCreationDtoToEntity_thenReturnExpectedGiftCertificate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();

        GiftCertificate actualCertificate = certificateMapper.mapCreationDtoToEntity(givenCreateDto);

        GiftCertificate expectedCertificate = getGiftCertificateMappedFromCreateDto();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenUpdateOnlySpecifiedFields() {
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();

        certificateMapper.updateEntityIgnoringTags(giftCertificateToUpdate, givenUpdateDto);

        GiftCertificate expectedCertificate = getGiftCertificateMergedWithUpdateDto();
        assertEquals(expectedCertificate, giftCertificateToUpdate);
    }

    @Test
    void givenUpdateDtoAndGiftCertificate_whenUpdateEntityIgnoreTags_thenNotUpdateTagList() {
        GiftCertificate giftCertificateToUpdate = getGiftCertificate();
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();

        certificateMapper.updateEntityIgnoringTags(giftCertificateToUpdate, givenUpdateDto);

        assertEquals(Collections.emptyList(), giftCertificateToUpdate.getTags(), "Tags need to stay unchanged");
    }
}
