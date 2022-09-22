package ru.clevertec.ecl.dto.request;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import ru.clevertec.ecl.dto.TagDto;

@Data
public class GiftCertificateCreateDto {

    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    private Set<TagDto> tags;
}
