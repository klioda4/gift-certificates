package ru.clevertec.ecl.config;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import javax.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.config.objects.ClusterProperties;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class NodesInfoConfig {

    private final ClusterProperties clusterProperties;

    @Bean
    public NodesInfo nodesInfo() {
        checkNoDuplicatesProvided(clusterProperties);
        List<List<Node>> shards = clusterProperties.getShards().stream()
                                      .map(shard -> shard.stream()
                                                        .map(this::buildNode)
                                                        .collect(toList()))
                                      .collect(toList());
        int currentShardIndex = getCurrentShardIndex();
        return new NodesInfo(shards, currentShardIndex, getCurrentNode(shards));
    }

    private boolean isCurrentNode(String nodeAddress) {
        return clusterProperties.getCurrentNode().equals(nodeAddress);
    }

    private Node getCurrentNode(List<List<Node>> shards) {
        return shards.stream()
                   .flatMap(Collection::stream)
                   .filter(Node::isCurrent)
                   .findFirst()
                   .orElseThrow(() -> new IllegalStateException("No current node is set"));
    }

    private Node buildNode(String nodeAddress) {
        return Node.builder()
                   .address(clusterProperties.getUrlPrefix() + nodeAddress)
                   .current(isCurrentNode(nodeAddress))
                   .build();
    }

    private int getCurrentShardIndex() {
        return IntStream.range(0, clusterProperties.getShards().size())
                   .filter(index -> clusterProperties.getShards().get(index).stream()
                                        .anyMatch(this::isCurrentNode))
                   .findFirst()
                   .orElseThrow(
                       () -> new ValidationException(
                           String.format("current-node property %s is incorrect - not exists in nodes list: %s",
                                         clusterProperties.getCurrentNode(),
                                         clusterProperties.getShards())));
    }

    private void checkNoDuplicatesProvided(ClusterProperties clusterProperties) {
        Set<String> uniqueAddresses = new HashSet<>();
        clusterProperties.getShards().stream()
            .flatMap(Collection::stream)
            .forEach(nodeAddress -> {
                boolean unique = uniqueAddresses.add(nodeAddress);
                if (!unique) {
                    throw new ValidationException("Node " + nodeAddress + " is defined more than once");
                }
            });
    }
}
