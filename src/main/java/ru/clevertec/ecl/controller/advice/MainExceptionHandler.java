package ru.clevertec.ecl.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.dto.response.ExceptionResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.IntegrityViolationException;
import ru.clevertec.ecl.exception.ValidationException;
import ru.clevertec.ecl.util.constant.ErrorDescription;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOtherException(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ErrorDescription currentErrorDescription = ErrorDescription.UNHANDLED_SERVER_ERROR;
        log.error(currentErrorDescription.getMessage(), e);
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, currentErrorDescription);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(Exception e) {
        log.info(e.getMessage());
        return getResponseEntity(HttpStatus.NOT_FOUND, ErrorDescription.OBJECT_NOT_EXISTS);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(Exception e) {
        log.info(e.getMessage());
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, -1, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder messageBuilder = new StringBuilder("Validation failed:");
        bindingResult.getFieldErrors()
            .forEach(fieldError -> {
                messageBuilder.append(", ");
                messageBuilder.append(fieldError.getField());
                messageBuilder.append(" - ");
                messageBuilder.append(fieldError.getDefaultMessage());
            });
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY,
                                 ErrorDescription.DTO_VALIDATION_FAILED.getCode(),
                                 messageBuilder.toString());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDataAccessException(Exception e) {
        log.error(e.getMessage(), e);
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ErrorDescription.DATABASE_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(Exception e) {
        log.warn(e.getMessage(), e);
        return getResponseEntity(HttpStatus.CONFLICT, ErrorDescription.DATABASE_UPDATE_FAILED);
    }

    @ExceptionHandler(IntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIntegrityViolationException(IntegrityViolationException e) {
        log.info(e.getMessage());
        return getResponseEntity(HttpStatus.CONFLICT, e.getErrorDescription().getCode(), e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(Exception e) {
        log.info(e.getMessage(), e);
        return getResponseEntity(HttpStatus.METHOD_NOT_ALLOWED,
                                 ErrorDescription.METHOD_NOT_SUPPORTED.getCode(),
                                 HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, int errorCode, String errorMessage) {
        return new ResponseEntity<>(
            ExceptionResponse.builder()
                .status(status.value())
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build(),
            status);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, ErrorDescription errorDescription) {
        return getResponseEntity(status, errorDescription.getCode(), errorDescription.getMessage());
    }
}
