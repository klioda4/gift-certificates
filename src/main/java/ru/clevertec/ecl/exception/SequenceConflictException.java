package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class SequenceConflictException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Sequence of %s was unexpected (%s, but required %s)";

    private final String entityName;
    private final long actualSequence;
    private final String requiredValue;

    public SequenceConflictException(String entityName, long actualSequence, String requiredValue) {
        super(String.format(MESSAGE_TEMPLATE, entityName, actualSequence, requiredValue));
        this.entityName = entityName;
        this.actualSequence = actualSequence;
        this.requiredValue = requiredValue;
    }
}
