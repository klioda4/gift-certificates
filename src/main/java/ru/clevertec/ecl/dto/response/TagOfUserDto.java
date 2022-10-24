package ru.clevertec.ecl.dto.response;

import lombok.Value;

@Value
public class TagOfUserDto {

    TagDto tag;
    Long userId;
}
