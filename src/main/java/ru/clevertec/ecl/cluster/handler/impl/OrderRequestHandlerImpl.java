package ru.clevertec.ecl.cluster.handler.impl;

import static java.util.stream.Collectors.toList;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.handler.OrderRequestHandler;
import ru.clevertec.ecl.cluster.handler.impl.util.CommitLogDistributor;
import ru.clevertec.ecl.cluster.handler.impl.util.ServletRequestUtil;
import ru.clevertec.ecl.cluster.handler.impl.util.dto.RequestInfo;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.RequestCheckHelper;
import ru.clevertec.ecl.cluster.util.RequestPathParser;
import ru.clevertec.ecl.cluster.util.calculation.ShardIdCalculator;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.dto.request.CommitLogStatusUpdateDto;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.exception.ServerDownException;
import ru.clevertec.ecl.util.constant.AppConstants;
import ru.clevertec.ecl.util.enums.CommitLogStatus;
import ru.clevertec.ecl.util.mapping.custom.EntityNameMapper;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderRequestHandlerImpl implements OrderRequestHandler {

    private static final String ACCEPT_REQUEST_LOG_MESSAGE = "Accept request by current node";

    private final RequestSender requestSender;
    private final NodesInfo nodesInfo;
    private final CommitLogDistributor commitLogDistributor;
    private final ShardIdCalculator shardIdCalculator;
    private final EntityNameMapper entityNameMapper;

    @Override
    public boolean doGetById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        int shardIndex = calculateShardIndexToForward(request);
        if (shardIndex == nodesInfo.getCurrentShardIndex()) {
            log.debug(ACCEPT_REQUEST_LOG_MESSAGE);
            return true;
        }
        List<Node> shard = nodesInfo.getShards().get(shardIndex);
        Node nodeToForward = getAliveNode(shard);
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request, nodeToForward.getAddress(), true);
        writeResponse(responseEntity, response);
        return false;
    }

    @Override
    public void doGetAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Object> allEntities = nodesInfo.getShards().stream()
                                       .map(this::getAliveNode)
                                       .map(node -> requestSender.forwardGetAll(request, node.getAddress()))
                                       .map(responseEntity -> Optional.ofNullable(responseEntity.getBody()))
                                       .map(optionalBody -> optionalBody
                                                                .orElseThrow(() -> new IllegalStateException(
                                                                    "Get all response isn't acquired")))
                                       .reduce(new ArrayList<>(),
                                               (list1, list2) -> {
                                                   list1.addAll(list2);
                                                   return list1;
                                               });
        ResponseEntity<String> responseEntity = ResponseEntity.ok(new Gson().toJson(allEntities));
        writeResponse(responseEntity, response);
    }

    @Override
    public boolean doCreate(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestInfo requestInfo = ServletRequestUtil.buildRequestInfo(request);
        String entityName = getEntityName(requestInfo.getServletPath());
        int shardIndex;
        if (RequestCheckHelper.isInterceptorRequest(request)) {
            shardIndex = nodesInfo.getCurrentShardIndex();
        } else {
            SequenceDto sequenceDto = calculateLeastCurrentSequence(entityName);
            shardIndex = shardIdCalculator.calculateShardIndexById(sequenceDto.getValue());
        }
        boolean isCurrentShard = (shardIndex == nodesInfo.getCurrentShardIndex());
        if (isCurrentShard) {
            CommitLogDto commitLogDto = commitLogDistributor.buildAndDistributeCommitLog(requestInfo,
                                                                                         this::getEntityNextSequence);
            String entityWithId = commitLogDto.getBody();
            request.setBody(entityWithId);
            request.setAttribute(InterceptorConstants.COMMIT_LOG_ID_ATTRIBUTE, commitLogDto.getId());
            return true;
        } else {
            redirectToAnotherShard(request, response, shardIndex);
            return false;
        }
    }

    @Override
    public boolean doModifyById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestInfo requestInfo = ServletRequestUtil.buildRequestInfo(request);
        long orderId = RequestPathParser.getIdFromServletPath(request.getServletPath());
        int shardIndex = shardIdCalculator.calculateShardIndexById(orderId);
        boolean isCurrentShard = (shardIndex == nodesInfo.getCurrentShardIndex());
        if (isCurrentShard) {
            CommitLogDto commitLogDto = commitLogDistributor.buildAndDistributeCommitLog(requestInfo,
                                                                                         (arg) -> orderId);
            request.setAttribute(InterceptorConstants.COMMIT_LOG_ID_ATTRIBUTE, commitLogDto.getId());
            return true;
        } else {
            redirectToAnotherShard(request, response, shardIndex);
            return false;
        }
    }

    @Override
    public void markCommitLogAsApplied(long commitLogId) {
        CommitLogStatusUpdateDto statusUpdateDto = new CommitLogStatusUpdateDto(CommitLogStatus.APPLIED);
        requestSender.sendRequest(HttpMethod.PATCH,
                                  nodesInfo.getCurrentNode().getAddress(),
                                  String.format(InterceptorConstants.COMMIT_LOG_UPDATE_STATUS_URI_PATTERN, commitLogId),
                                  new Gson().toJson(statusUpdateDto));
    }

    private int calculateShardIndexToForward(HttpServletRequest request) {
        long orderId = RequestPathParser.getIdFromServletPath(request.getServletPath());
        return shardIdCalculator.calculateShardIndexById(orderId);
    }

    private void redirectToAnotherShard(CachedHttpServletRequest request,
                                        HttpServletResponse response,
                                        int shardIndex) throws IOException {
        List<Node> shardToForward = nodesInfo.getShards().get(shardIndex);
        Node aliveNode = getAliveNode(shardToForward);
        ResponseEntity<String> responseEntity = requestSender.forwardRequest(request,
                                                                             aliveNode.getAddress(),
                                                                             false);
        writeResponse(responseEntity, response);
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

    private Node getAliveNode(List<Node> shard) {
        return shard.stream()
                   .filter(Node::isActive)
                   .findFirst()
                   .orElseThrow(() -> new ServerDownException(shard.stream()
                                                                  .map(Node::getAddress)
                                                                  .collect(toList())));
    }

    private SequenceDto calculateLeastCurrentSequence(String entityName) {
        String currentSequenceUri = String.format(InterceptorConstants.CURRENT_SEQUENCE_URI_PATTERN, entityName);
        return nodesInfo.getShards().stream()
                   .map(this::getAliveNode)
                   .map(node -> requestSender.requestSequence(node.getAddress(), currentSequenceUri))
                   .min(Comparator.comparingLong(SequenceDto::getValue))
                   .orElseThrow(() -> new ServerDownException(collectDownServers()));
    }

    private List<String> collectDownServers() {
        return nodesInfo.getShards().stream()
                   .flatMap(Collection::stream)
                   .filter(node -> !node.isActive())
                   .map(Node::getAddress)
                   .collect(toList());
    }

    private long getEntityNextSequence(String entityName) {
        String entityNextSequenceUri = String.format(InterceptorConstants.NEXT_SEQUENCE_URI_PATTERN_, entityName);
        SequenceDto sequenceDto = requestSender.requestSequence(nodesInfo.getCurrentNode().getAddress(),
                                                                entityNextSequenceUri);
        return sequenceDto.getValue();
    }

    private String getEntityName(String servletPath) {
        String rawEntityName = RequestPathParser.getRawEntityNameFromServletPath(servletPath);
        return entityNameMapper.mapControllerBindingNameToEntityName(rawEntityName);
    }
}
