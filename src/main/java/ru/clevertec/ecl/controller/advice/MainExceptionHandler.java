package ru.clevertec.ecl.controller.advice;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.clevertec.ecl.dto.response.ExceptionResponse;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.IntegrityViolationException;
import ru.clevertec.ecl.util.constant.ErrorDescription;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOtherException(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ErrorDescription errorDescription = ErrorDescription.UNHANDLED_SERVER_ERROR;
        log.error(errorDescription.getDefaultMessage(), e);
        return getResponseEntity(errorDescription);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(Exception e) {
        log.info(e.getMessage());
        return getResponseEntity(ErrorDescription.ENTITY_NOT_EXISTS.getStatusCode(),
                                 ErrorDescription.ENTITY_NOT_EXISTS.getErrorCode(),
                                 e.getMessage());
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
        String answerMessage = messageBuilder.toString();
        log.info("Message to answer: {}", answerMessage);
        return getResponseEntity(ErrorDescription.DTO_VALIDATION_FAILED.getStatusCode(),
                                 ErrorDescription.DTO_VALIDATION_FAILED.getErrorCode(),
                                 answerMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.info(e.getMessage());
        return getResponseEntity(ErrorDescription.ARGUMENT_VALIDATION_FAILED.getStatusCode(),
                                 ErrorDescription.ARGUMENT_VALIDATION_FAILED.getErrorCode(),
                                 e.getMessage());
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> handleExceptionOfIncorrectOrMissingArgument(Exception e) {
        log.info(e.getMessage());
        return getResponseEntity(ErrorDescription.INCORRECT_OR_MISSING_ARGUMENT.getStatusCode(),
                                 ErrorDescription.INCORRECT_OR_MISSING_ARGUMENT.getErrorCode(),
                                 e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDataAccessException(Exception e) {
        log.error(e.getMessage(), e);
        return getResponseEntity(ErrorDescription.DATABASE_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(Exception e) {
        log.warn(e.getMessage(), e);
        return getResponseEntity(ErrorDescription.DATABASE_UPDATE_FAILED);
    }

    @ExceptionHandler(IntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIntegrityViolationException(IntegrityViolationException e) {
        log.info(e.getMessage());
        return getResponseEntity(e.getErrorDescription().getStatusCode(),
                                 e.getErrorDescription().getErrorCode(),
                                 e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(Exception e) {
        log.info(e.getMessage(), e);
        return getResponseEntity(ErrorDescription.METHOD_NOT_SUPPORTED.getStatusCode(),
                                 ErrorDescription.METHOD_NOT_SUPPORTED.getErrorCode(),
                                 HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, int errorCode, String errorMessage) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                                        .status(status.value())
                                        .errorCode(errorCode)
                                        .errorMessage(errorMessage)
                                        .build(),
                                    status);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(int statusCode, int errorCode, String errorMessage) {
        return getResponseEntity(HttpStatus.valueOf(statusCode), errorCode, errorMessage);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(ErrorDescription errorDescription) {
        return getResponseEntity(HttpStatus.valueOf(errorDescription.getStatusCode()),
                                 errorDescription.getErrorCode(),
                                 errorDescription.getDefaultMessage());
    }
}
