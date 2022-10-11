package ru.clevertec.ecl.dto.request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GiftCertificateFilterDto {

    String tagName;
    String nameSample;
    String descriptionSample;
}
