package ru.clevertec.ecl.cluster.nodeInfo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cluster")
public class NodesInfo {

    private List<Node> nodes;

    // TODO: make camelCase
    private int currentnodeindex;
}
