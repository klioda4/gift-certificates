package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    Long id;
    String name;
}
