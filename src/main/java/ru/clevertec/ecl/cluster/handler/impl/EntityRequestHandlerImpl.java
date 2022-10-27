package ru.clevertec.ecl.cluster.handler.impl;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.handler.EntityRequestHandler;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;

@Component
public class EntityRequestHandlerImpl implements EntityRequestHandler {

    private final List<Node> nodes;
    private final Node currentNode;

    private final RequestSender requestSender;

    public EntityRequestHandlerImpl(NodesInfo nodesInfo, RequestSender requestSender) {
        nodes = nodesInfo.getNodes();
        currentNode = nodes.get(nodesInfo.getCurrentnodeindex());
        this.requestSender = requestSender;
    }

    @Override
    public void distributeRequest(CachedHttpServletRequest request, HttpServletResponse response) {
        nodes.stream()
            .filter(node -> node != currentNode)
            .forEach(node -> requestSender.forwardRequest(request, node));
    }
}
