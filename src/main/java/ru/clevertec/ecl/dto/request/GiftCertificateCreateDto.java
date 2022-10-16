package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GiftCertificateCreateDto {

    String name;
    String description;
    BigDecimal price;
    int duration;
    List<String> tagNames;
}
