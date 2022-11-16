package ru.clevertec.ecl.repository.sequence;

public interface SequenceRepository {

    long getNextSequence(String entityName);

    long getCurrentSequence(String entityName);

    void setCurrentSequence(String entityName, long newValue);
}
