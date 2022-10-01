package ru.clevertec.ecl.test.supply;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.model.GiftCertificate;

public class GiftCertificateTestDataSupplier {

    public static GiftCertificate getGiftCertificate() {
        return GiftCertificate.builder()
            .id(1L)
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .duration(30)
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .build();
    }

    public static GiftCertificateDto getGiftCertificateDto() {
        return GiftCertificateDto.builder()
            .id(1L)
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .duration(30)
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .tags(Collections.emptyList())
            .build();
    }

    public static GiftCertificateCreateDto getGiftCertificateCreateDto() {
        return GiftCertificateCreateDto.builder()
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .duration(30)
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
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .duration(30)
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .build();
    }

    public static GiftCertificate getGiftCertificateMergedWithUpdateDto() {
        return GiftCertificate.builder()
            .id(1L)
            .name("new_name")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .duration(55)
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .build();
    }

    public static LocalDateTime getDefaultCreateDate() {
        return LocalDateTime.of(2050, 1, 1, 0, 0);
    }
}
