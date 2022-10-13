package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiftCertificatePriceUpdateDto {

    @PositiveOrZero
    @NotNull
    BigDecimal price;
}
