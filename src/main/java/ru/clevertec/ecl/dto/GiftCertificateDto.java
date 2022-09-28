package ru.clevertec.ecl.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.ecl.util.serialization.jackson.LocalDateTimeJsonSerializer;

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

    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime createDate;

    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime lastUpdateDate;

    private List<TagDto> tags;
}
