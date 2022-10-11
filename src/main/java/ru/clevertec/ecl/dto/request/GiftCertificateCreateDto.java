package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftCertificateCreateDto {

    @NotBlank
    String name;

    @NotBlank
    String description;

    @Positive
    BigDecimal price;

    @Positive
    int duration;

    List<@NotBlank String> tagNames;
}