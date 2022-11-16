package ru.clevertec.ecl.cluster.nodeInfo;

import java.util.List;
import lombok.Value;

@Value
public class NodesInfo {

    List<List<Node>> shards;

    int currentShardIndex;
    Node currentNode;
}
