package ru.clevertec.ecl.cluster.schedule;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledHealthChecker {

    private static final long CHECK_RATE = 60;
    private static final long INITIAL_DELAY = 30;

    private final NodesInfo nodesInfo;
    private final RequestSender requestSender;

    @Scheduled(initialDelay = INITIAL_DELAY,
               fixedRate = CHECK_RATE,
               timeUnit = TimeUnit.SECONDS)
    public void checkCluster() {
        nodesInfo.getShards().stream()
            .flatMap(Collection::stream)
            .filter(node -> !node.isCurrent())
            .peek(node -> log.debug("Check node {}", node.getAddress()))
            .filter(node -> {
                boolean active = requestSender.checkHealth(node.getAddress()).isActive();
                return !active;
            })
            .peek(node -> log.info("Mark node {} as down", node.getAddress()))
            .forEach(node -> node.setActive(false));
    }
}
