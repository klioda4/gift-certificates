package ru.clevertec.ecl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDto {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;

    private Set<TagDto> tags;
}
