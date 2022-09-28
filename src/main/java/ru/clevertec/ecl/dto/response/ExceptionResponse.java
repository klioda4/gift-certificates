package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {

    private int status;
    private int errorCode;
    private String errorMessage;
}
