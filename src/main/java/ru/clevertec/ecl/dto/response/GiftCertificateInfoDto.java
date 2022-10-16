package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftCertificateInfoDto {

    Long id;
    String name;
    String description;
}
