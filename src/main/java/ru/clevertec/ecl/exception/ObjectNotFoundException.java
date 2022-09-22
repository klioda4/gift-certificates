package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "%s not found with %s=%s";

    private final String objectClassName;
    private final String searchField;
    private final Object searchValue;

    public ObjectNotFoundException(String objectClassName, String searchField, Object searchValue) {
        super(getFormattedMessage(searchField, searchValue, objectClassName));
        this.searchField = searchField;
        this.searchValue = searchValue;
        this.objectClassName = objectClassName;
    }

    private static String getFormattedMessage(String searchField, Object searchValue,
        String objectClassName) {

        return String.format(MESSAGE_TEMPLATE,
            objectClassName == null ? "Object" : objectClassName,
            searchField == null ? "value" : searchField,
            searchValue == null ? "" : searchValue);
    }
}
