package ru.clevertec.ecl.cluster.handler.impl;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.handler.OrderRequestHandler;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.IndexCarousel;
import ru.clevertec.ecl.cluster.util.RequestPathParser;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.util.constant.AppConstants;

@Slf4j
@Component
public class OrderRequestHandlerImpl implements OrderRequestHandler {

    private static final String ACCEPT_REQUEST_LOG_MESSAGE = "Accept request by current node";

    private final List<Node> nodes;
    private final Node currentNode;

    private final RequestSender requestSender;
    private final IndexCarousel nodeIndexCarousel;

    public OrderRequestHandlerImpl(RequestSender requestSender, NodesInfo nodesInfo) {
        this.requestSender = requestSender;
        nodes = nodesInfo.getNodes();
        currentNode = nodes.get(nodesInfo.getCurrentnodeindex());
        nodeIndexCarousel = new IndexCarousel(0,
                                              nodes.size() - 1,
                                              nodesInfo.getCurrentnodeindex());
    }

    @Override
    public boolean doGetById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        Node nodeToForward = getNodeToForward(request);
        if (nodeToForward == currentNode) {
            log.debug(ACCEPT_REQUEST_LOG_MESSAGE);
            return true;
        }
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request, nodeToForward);
        writeResponse(responseEntity, response);
        return false;
    }

    @Override
    public void doGetAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Object> allEntities = nodes.stream()
                                       .map(node -> requestSender.forwardGetAll(request, node))
                                       .map(responseEntity -> Optional.ofNullable(responseEntity.getBody()))
                                       .map(optionalBody -> optionalBody
                                                                .orElseThrow(() -> new IllegalStateException(
                                                                    "Get all response isn't acquired")))
                                       .reduce(new ArrayList<>(),
                                               (list1, list2) -> {
                                                   list1.addAll(list2);
                                                   return list1;
                                               });
        ResponseEntity<String> responseEntity = new ResponseEntity<>(new Gson().toJson(allEntities),
                                                                     HttpStatus.OK);
        writeResponse(responseEntity, response);
    }

    @Override
    public boolean doUpdateById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        Node nodeToForward = getNodeToForward(request);
        if (nodeToForward == currentNode) {
            log.debug(ACCEPT_REQUEST_LOG_MESSAGE);
            return true;
        }
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request, nodeToForward);
        writeResponse(responseEntity, response);
        return false;
    }

    @Override
    public boolean doCreate(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        Node nodeToForward = getNodeToSendCreate();
        if (nodeToForward == currentNode) {
            log.debug(ACCEPT_REQUEST_LOG_MESSAGE);
            return true;
        }
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request, nodeToForward);
        writeResponse(responseEntity, response);
        return false;
    }

    @Override
    public boolean doDelete(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        Node nodeToForward = getNodeToForward(request);
        if (nodeToForward == currentNode) {
            log.debug(ACCEPT_REQUEST_LOG_MESSAGE);
            return true;
        }
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request, nodeToForward);
        writeResponse(responseEntity, response);
        return false;
    }

    private Node getNodeToForward(HttpServletRequest request) {
        long orderId = RequestPathParser.getIdFromServletPath(request.getServletPath());
        return nodes.get((int) (orderId % nodes.size()));
    }

    private Node getNodeToSendCreate() {
        return nodes.get(nodeIndexCarousel.getNext());
    }

    @SuppressWarnings("ConstantConditions")
    private void writeResponse(ResponseEntity<String> responseEntity, HttpServletResponse response) throws IOException {
        response.setStatus(responseEntity.getStatusCodeValue());
        if (responseEntity.hasBody()) {
            response.setCharacterEncoding(AppConstants.ENCODING);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (PrintWriter responseWriter = response.getWriter()) {
                responseWriter.write(responseEntity.getBody());
            }
        }
    }
}
