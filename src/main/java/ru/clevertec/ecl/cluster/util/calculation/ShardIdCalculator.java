package ru.clevertec.ecl.cluster.util.calculation;

public interface ShardIdCalculator {

    int calculateShardIndexById(long id);
}
