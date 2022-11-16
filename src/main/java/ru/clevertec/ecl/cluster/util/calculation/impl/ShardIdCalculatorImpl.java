package ru.clevertec.ecl.cluster.util.calculation.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.util.calculation.ShardIdCalculator;

@RequiredArgsConstructor
@Component
public class ShardIdCalculatorImpl implements ShardIdCalculator {

    private final NodesInfo nodesInfo;

    @Override
    public int calculateShardIndexById(long id) {
        return (int) (id % nodesInfo.getShards().size());
    }
}
