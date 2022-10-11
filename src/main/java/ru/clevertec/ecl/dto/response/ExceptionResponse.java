package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {

    int status;
    int errorCode;
    String errorMessage;
}
