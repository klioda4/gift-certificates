package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftCertificateCreateDto {

    @Positive
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @PositiveOrZero
    @NotNull
    BigDecimal price;

    @PositiveOrZero
    @NotNull
    Integer duration;

    @NotEmpty
    List<@NotBlank String> tagNames;
}
