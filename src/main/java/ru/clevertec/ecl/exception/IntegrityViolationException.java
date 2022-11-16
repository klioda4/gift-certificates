package ru.clevertec.ecl.exception;

import lombok.Getter;
import ru.clevertec.ecl.util.constant.ErrorDescription;

@Getter
public class IntegrityViolationException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Field value of %s (%s=%s) violates data integrity: %s";

    private final String objectClassName;
    private final String fieldName;
    private final Object value;
    private final ErrorDescription errorDescription;

    public IntegrityViolationException(String objectClassName, String fieldName, Object value,
                                       ErrorDescription errorDescription) {
        super(getFormattedMessage(objectClassName, fieldName, value, errorDescription.getDefaultMessage()));
        this.objectClassName = objectClassName;
        this.fieldName = fieldName;
        this.value = value;
        this.errorDescription = errorDescription;
    }

    public IntegrityViolationException(Throwable cause, String objectClassName, String fieldName, Object value,
                                       ErrorDescription errorDescription) {
        super(getFormattedMessage(objectClassName, fieldName, value, errorDescription.getDefaultMessage()),
              cause);
        this.objectClassName = objectClassName;
        this.fieldName = fieldName;
        this.value = value;
        this.errorDescription = errorDescription;
    }

    private static String getFormattedMessage(String objectClassName, String fieldName, Object value, String reason) {
        return String.format(MESSAGE_TEMPLATE, objectClassName, fieldName, value, reason);
    }
}
