package ru.clevertec.ecl.util.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDescription {

    INCORRECT_OR_MISSING_ARGUMENT(400, 40001, ""),
    INVALID_REQUEST_PATH_OR_METHOD(400, 40002, "Request cannot be recognized, check URL and http method"),
    ENTITY_NOT_EXISTS(404, 40401, "Requested object not found"),
    METHOD_NOT_SUPPORTED(405, 40501, ""),
    DATABASE_UPDATE_FAILED(409, 40901, "Can't update data without integrity breach, check the request or try later"),
    TAG_ALREADY_EXISTS(409, 40902, "Tag with specified name already exists"),
    SEQUENCE_CONFLICT(409, 40903, ""),
    DTO_VALIDATION_FAILED(422, 42201, ""),
    ARGUMENT_VALIDATION_FAILED(422, 42202, ""),
    UNHANDLED_SERVER_ERROR(500, 50001, "Unhandled server error, try to request later or contact support"),
    DATABASE_ERROR(500, 50002, "Database problem, try to request later or contact support"),
    NODE_CONNECTION_ERROR(500, 50003,
                          "Required servers are currently unavailable, try to request later or contact support");

    private final int statusCode;
    private final int errorCode;
    private final String defaultMessage;
}
