package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class GiftCertificateCreationDto {

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    private List<String> tags;
}
