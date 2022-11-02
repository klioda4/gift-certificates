package ru.clevertec.ecl.dto.request;

import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderCreateDto {

    @Positive
    Long userId;

    @Positive
    Long giftCertificateId;
}
