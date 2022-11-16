package ru.clevertec.ecl.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SequenceDto {

    long value;

    @JsonCreator
    public SequenceDto(@JsonProperty("value") long value) {
        this.value = value;
    }
}
