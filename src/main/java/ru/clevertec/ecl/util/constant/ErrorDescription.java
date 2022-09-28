package ru.clevertec.ecl.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDescription {

    UNKNOWN_SERVER_EXCEPTION(-1, "Unhandled server error, try to request later"),
    OBJECT_NOT_EXISTS(40401, "Requested object not found");

    private final int code;
    private final String message;
}
