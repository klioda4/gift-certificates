package ru.clevertec.ecl.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.dto.response.ExceptionResponse;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.util.constant.ErrorDescription;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ErrorDescription currentErrorDescription = ErrorDescription.UNKNOWN_SERVER_EXCEPTION;
        log.error(currentErrorDescription.getMessage(), e);
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, currentErrorDescription);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(ObjectNotFoundException e) {
        log.info(e.getMessage());
        return getResponseEntity(HttpStatus.NOT_FOUND, ErrorDescription.OBJECT_NOT_EXISTS);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, ErrorDescription errorDescription) {
        return new ResponseEntity<>(
            ExceptionResponse.builder()
                .status(status.value())
                .errorCode(errorDescription.getCode())
                .errorMessage(errorDescription.getMessage())
                .build(),
            status);
    }
}
