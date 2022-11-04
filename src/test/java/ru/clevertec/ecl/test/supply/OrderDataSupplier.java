package ru.clevertec.ecl.test.supply;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateInfoDto;
import ru.clevertec.ecl.dto.response.OrderDto;
import ru.clevertec.ecl.model.Order;

public class OrderDataSupplier {

    public static final Long DEFAULT_ID = 1L;
    public static final BigDecimal DEFAULT_COST = BigDecimal.valueOf(5L);
    public static final Integer DEFAULT_DURATION = 30;
    public static final LocalDateTime DEFAULT_PURCHASE_DATE = LocalDateTime.of(2050, 1, 1, 0, 0);

    public static Order getOrder() {
        return Order.builder()
                   .id(DEFAULT_ID)
                   .cost(DEFAULT_COST)
                   .duration(DEFAULT_DURATION)
                   .purchaseDate(DEFAULT_PURCHASE_DATE)
                   .user(UserDataSupplier.getUser())
                   .giftCertificate(GiftCertificateDataSupplier.getGiftCertificate())
                   .build();
    }

    public static Order getOrderMappedFromCreateOrder(LocalDateTime purchaseDate) {
        return Order.builder()
                   .cost(DEFAULT_COST)
                   .duration(DEFAULT_DURATION)
                   .purchaseDate(purchaseDate)
                   .user(UserDataSupplier.getUser())
                   .giftCertificate(GiftCertificateDataSupplier.getGiftCertificate())
                   .build();
    }

    public static OrderDto getOrderDto() {
        return OrderDto.builder()
                   .id(DEFAULT_ID)
                   .cost(DEFAULT_COST)
                   .duration(DEFAULT_DURATION)
                   .purchaseDate(DEFAULT_PURCHASE_DATE)
                   .giftCertificate(GiftCertificateInfoDto.builder()
                                        .id(GiftCertificateDataSupplier.DEFAULT_ID)
                                        .name(GiftCertificateDataSupplier.DEFAULT_NAME)
                                        .description(GiftCertificateDataSupplier.DEFAULT_DESCRIPTION)
                                        .build())
                   .build();
    }

    public static OrderCreateDto getOrderCreateDto() {
        return OrderCreateDto.builder()
                   .userId(1L)
                   .giftCertificateId(1L)
                   .build();
    }
}
