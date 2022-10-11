package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Incorrect value of field \"%s\": %s";

    private final String fieldName;
    private final String violationMessage;

    public ValidationException(String fieldName, String violationMessage) {
        super(String.format(MESSAGE_TEMPLATE, fieldName, violationMessage));
        this.fieldName = fieldName;
        this.violationMessage = violationMessage;
    }
}
