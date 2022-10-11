package ru.clevertec.ecl.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionResponse {

    int status;
    int errorCode;
    String errorMessage;
}
