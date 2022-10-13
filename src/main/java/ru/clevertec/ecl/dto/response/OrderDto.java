package ru.clevertec.ecl.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * Dto to provide info about order without including user reference
 */
@Value
@Builder
public class OrderDto {

    Long id;
    BigDecimal cost;
    Integer duration;
    LocalDateTime purchaseDate;
    GiftCertificateInfoDto giftCertificate;
}
