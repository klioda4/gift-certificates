package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import ru.clevertec.ecl.util.validate.constraint.NotBlankOrNull;

@Value
@Builder
public class GiftCertificateUpdateDto {

    @NotBlankOrNull
    String name;

    @NotBlankOrNull
    String description;

    BigDecimal price;

    Integer duration;

    List<@NotBlank String> tagNames;
}
