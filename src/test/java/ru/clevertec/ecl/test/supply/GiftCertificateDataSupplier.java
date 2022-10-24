package ru.clevertec.ecl.test.supply;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.model.GiftCertificate;

public class GiftCertificateDataSupplier {

    public static final long DEFAULT_ID = 1L;
    public static final String DEFAULT_NAME = "cert";
    public static final String DEFAULT_DESCRIPTION = "description";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(5L);
    public static final int DEFAULT_DURATION = 30;
    public static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(2050, 1, 1, 0, 0);

    public static GiftCertificate getGiftCertificate() {
        return GiftCertificate.builder()
            .id(DEFAULT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION)
            .createDate(DEFAULT_DATE)
            .lastUpdateDate(DEFAULT_DATE)
            .build();
    }

    public static GiftCertificateDto getGiftCertificateDto() {
        return GiftCertificateDto.builder()
            .id(DEFAULT_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION)
            .createDate(DEFAULT_DATE)
            .lastUpdateDate(DEFAULT_DATE)
            .tags(Collections.emptyList())
            .build();
    }

    public static GiftCertificateCreateDto getGiftCertificateCreateDto() {
        return GiftCertificateCreateDto.builder()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION)
            .tagNames(Collections.emptyList())
            .build();
    }

    public static GiftCertificateUpdateDto getGiftCertificateUpdateDto() {
        return GiftCertificateUpdateDto.builder()
            .name("new_name")
            .duration(55)
            .build();
    }

    public static GiftCertificate getGiftCertificateMappedFromCreateDto() {
        return GiftCertificate.builder()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .duration(DEFAULT_DURATION)
            .createDate(DEFAULT_DATE)
            .lastUpdateDate(DEFAULT_DATE)
            .build();
    }

    public static GiftCertificate getGiftCertificateMergedWithUpdateDto() {
        return GiftCertificate.builder()
            .name("new_name")
            .duration(55)
            .id(DEFAULT_ID)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .createDate(DEFAULT_DATE)
            .lastUpdateDate(DEFAULT_DATE)
            .build();
    }

    public static List<GiftCertificate> getListOfSingleGiftCertificate() {
        return Collections.singletonList(getGiftCertificate());
    }
}
