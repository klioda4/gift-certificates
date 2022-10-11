package ru.clevertec.ecl.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class TagPutDto {

    @NotBlank
    String name;
}
