package ru.clevertec.ecl.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDescription {

    OBJECT_NOT_EXISTS(40401, "Requested object not found"),
    METHOD_NOT_SUPPORTED(40501, ""),
    DATABASE_UPDATE_FAILED(40901, "Can't update data without integrity breach, check the request or try later"),
    TAG_ALREADY_EXISTS(40902, "Tag with specified name already exists"),
    DTO_VALIDATION_FAILED(42201, ""),
    UNHANDLED_SERVER_ERROR(50001, "Unhandled server error, try to request later or contact support");

    private final int code;
    private final String message;
}
