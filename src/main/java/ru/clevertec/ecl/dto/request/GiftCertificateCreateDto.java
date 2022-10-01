package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateCreateDto {

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private List<String> tagNames;
}
