package ru.clevertec.ecl.dto.response;

import lombok.Builder;
import lombok.Value;
import ru.clevertec.ecl.util.enums.CommitLogStatus;

@Value
@Builder(toBuilder = true)
public class CommitLogDto {

    Long id;
    String httpMethod;
    long entityId;
    String entityName;
    String body;
    CommitLogStatus status;
}
