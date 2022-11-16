package ru.clevertec.ecl.cluster.handler.impl.util;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.clevertec.ecl.cluster.handler.impl.util.dto.RequestInfo;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.util.RequestPathParser;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.util.enums.CommitLogStatus;
import ru.clevertec.ecl.util.mapping.custom.EntityNameMapper;

@RequiredArgsConstructor
@Slf4j
@Component
public class CommitLogDistributor {

    private static final String MAX_NUMBER_OF_REPEATS_REACHED_MESSAGE =
        "Could not distribute request: max number of repeats reached.";

    private static final String ID_FIELD = "id";
    private static final String UPDATE_SEQUENCES_PARAMETER = "updateSequences";
    private static final int MAX_NUMBER_OF_REPEATS = 10;

    private final RequestSender requestSender;
    private final NodesInfo nodesInfo;
    private final EntityNameMapper entityNameMapper;

    @SuppressWarnings("unchecked")
    public CommitLogDto buildAndDistributeCommitLog(RequestInfo requestInfo,
                                                    Function<String, Long> getNextEntityId) {
        Map<String, String> entity = new Gson().fromJson(requestInfo.getBody(), Map.class);
        String entityName = getEntityName(requestInfo.getServletPath());
        RequestInfo updatedRequestInfo;
        CommitLogDto newCommitLogDto;
        boolean successCommitLogSave;
        int numberOfRepeats = 0;
        do {
            long entityId = getNextEntityId.apply(entityName);
            entity.put(ID_FIELD, Long.toString(entityId));
            String updatedBody = new Gson().toJson(entity);
            updatedRequestInfo = requestInfo.toBuilder()
                                     .body(updatedBody)
                                     .build();
            newCommitLogDto = saveNewCommitLogToCurrentNode(updatedRequestInfo, entityId, entityName);
            successCommitLogSave = distributeCommitLog(newCommitLogDto);
            if (successCommitLogSave) {
                distributeApplyCommitLogOrDeclineIfError(newCommitLogDto.getId());
            } else {
                distributeDeclineCommitLog(newCommitLogDto.getId());
            }

            numberOfRepeats++;
            if (numberOfRepeats == MAX_NUMBER_OF_REPEATS) {
                log.warn(MAX_NUMBER_OF_REPEATS_REACHED_MESSAGE);
                throw new IllegalStateException(MAX_NUMBER_OF_REPEATS_REACHED_MESSAGE);
            }
        } while (!successCommitLogSave);
        return newCommitLogDto;
    }

    private CommitLogDto saveNewCommitLogToCurrentNode(RequestInfo requestInfo, long entityId, String entityName) {
        long commitLogId = getCommitLogNextSequence();
        CommitLogDto commitLogDto = buildCommitLog(commitLogId, entityId, entityName, requestInfo);
        sendCommitLogCreation(nodesInfo.getCurrentNode().getAddress(),
                              commitLogDto,
                              false);
        return commitLogDto;
    }

    private boolean distributeCommitLog(CommitLogDto commitLogDto) {
        log.debug("Distributing to all nodes commit log: {}", commitLogDto);
        return nodesInfo.getShards().stream()
                   .flatMap(Collection::stream)
                   .filter(Node::isActive)
                   .filter(node -> !node.isCurrent())
                   .map(node -> {
                       try {
                           return sendCommitLogCreation(node.getAddress(), commitLogDto, true);
                       } catch (WebClientResponseException e) {
                           return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
                       }
                   })
                   .map(ResponseEntity::getStatusCode)
                   .allMatch(httpStatus -> (httpStatus == HttpStatus.CREATED));
    }

    private long getCommitLogNextSequence() {
        String nextSequenceUri = String.format(InterceptorConstants.NEXT_SEQUENCE_URI_PATTERN_,
                                               InterceptorConstants.COMMIT_LOG_ENTITY_NAME);
        SequenceDto sequenceDto = requestSender.requestSequence(nodesInfo.getCurrentNode().getAddress(),
                                                                nextSequenceUri);
        return sequenceDto.getValue();
    }

    private ResponseEntity<String> sendCommitLogCreation(String host,
                                                         CommitLogDto commitLogDto,
                                                         boolean updateSequences) {
        String parametrizedUri = String.format("%s?%s=%b",
                                               InterceptorConstants.COMMIT_LOG_URI,
                                               UPDATE_SEQUENCES_PARAMETER,
                                               updateSequences);
        return requestSender.sendRequest(HttpMethod.POST,
                                         host,
                                         parametrizedUri,
                                         new Gson().toJson(commitLogDto));
    }

    private CommitLogDto buildCommitLog(long commitLogId, long entityId, String entityName, RequestInfo requestInfo) {
        return CommitLogDto.builder()
                   .id(commitLogId)
                   .httpMethod(requestInfo.getHttpMethod().name())
                   .entityId(entityId)
                   .entityName(entityName)
                   .body(requestInfo.getBody())
                   .status(CommitLogStatus.CREATED)
                   .build();
    }

    private void distributeApplyCommitLog(long commitLogId) {
        log.debug("Distribute apply to all nodes");
        String finishUri = String.format(InterceptorConstants.COMMIT_LOG_APPLY_URI_PATTERN,
                                         commitLogId);
        nodesInfo.getShards().stream()
            .flatMap(Collection::stream)
            .filter(Node::isActive)
            .filter(node -> !node.isCurrent())
            .peek(node -> log.debug("Send apply to {}", node))
            .forEach(node -> requestSender.sendRequest(HttpMethod.POST,
                                                       node.getAddress(),
                                                       finishUri,
                                                       null));
    }

    private void distributeDeclineCommitLog(long commitLogId) {
        log.debug("Distribute decline to all nodes");
        String finishUri = String.format(InterceptorConstants.COMMIT_LOG_DECLINE_URI_PATTERN,
                                         commitLogId);
        nodesInfo.getShards().stream()
            .flatMap(Collection::stream)
            .filter(Node::isActive)
            .filter(node -> !node.isCurrent())
            .peek(node -> log.debug("Send decline to {}", node))
            .forEach(node -> requestSender.sendRequest(HttpMethod.POST,
                                                       node.getAddress(),
                                                       finishUri,
                                                       null));
    }

    private void distributeApplyCommitLogOrDeclineIfError(long commitLogId) {
        try {
            distributeApplyCommitLog(commitLogId);
        } catch (WebClientResponseException e) {
            log.info("Conflict while applying commit log.");
            distributeDeclineCommitLog(commitLogId);
            throw e;
        }
    }

    private String getEntityName(String servletPath) {
        String rawEntityName = RequestPathParser.getRawEntityNameFromServletPath(servletPath);
        return entityNameMapper.mapControllerBindingNameToEntityName(rawEntityName);
    }
}
