package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderPutDto {

    @PositiveOrZero
    @NotNull
    BigDecimal cost;

    @PositiveOrZero
    @NotNull
    Integer duration;
}
