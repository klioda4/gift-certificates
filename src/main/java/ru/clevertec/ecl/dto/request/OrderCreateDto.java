package ru.clevertec.ecl.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderCreateDto {

    @NotNull
    Long userId;

    @NotNull
    Long giftCertificateId;
}
