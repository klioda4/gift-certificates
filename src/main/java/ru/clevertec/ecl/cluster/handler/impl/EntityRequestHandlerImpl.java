package ru.clevertec.ecl.cluster.handler.impl;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.handler.EntityRequestHandler;
import ru.clevertec.ecl.cluster.handler.impl.util.CommitLogDistributor;
import ru.clevertec.ecl.cluster.handler.impl.util.ServletRequestUtil;
import ru.clevertec.ecl.cluster.handler.impl.util.dto.RequestInfo;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.RequestPathParser;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.dto.request.CommitLogStatusUpdateDto;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.util.enums.CommitLogStatus;

@RequiredArgsConstructor
@Slf4j
@Component
public class EntityRequestHandlerImpl implements EntityRequestHandler {

    private final RequestSender requestSender;
    private final NodesInfo nodesInfo;
    private final CommitLogDistributor commitLogDistributor;

    @Override
    public void doCreate(CachedHttpServletRequest request, HttpServletResponse response) {
        RequestInfo requestInfo = ServletRequestUtil.buildRequestInfo(request);
        CommitLogDto commitLogDto = commitLogDistributor.buildAndDistributeCommitLog(requestInfo,
                                                                                     this::getEntityNextSequence);
        String entityWithId = commitLogDto.getBody();
        request.setBody(entityWithId);
        request.setAttribute(InterceptorConstants.COMMIT_LOG_ID_ATTRIBUTE, commitLogDto.getId());
    }

    @Override
    public void doModifyingById(CachedHttpServletRequest request, HttpServletResponse response) {
        RequestInfo requestInfo = ServletRequestUtil.buildRequestInfo(request);
        long entityId = RequestPathParser.getIdFromServletPath(request.getServletPath());
        CommitLogDto commitLogDto = commitLogDistributor.buildAndDistributeCommitLog(requestInfo,
                                                                                     (arg) -> entityId);
        request.setAttribute(InterceptorConstants.COMMIT_LOG_ID_ATTRIBUTE, commitLogDto.getId());
    }

    @Override
    public void markCommitLogAsApplied(long commitLogId) {
        CommitLogStatusUpdateDto statusUpdateDto = new CommitLogStatusUpdateDto(CommitLogStatus.APPLIED);
        requestSender.sendRequest(HttpMethod.PATCH,
                                  nodesInfo.getCurrentNode().getAddress(),
                                  String.format(InterceptorConstants.COMMIT_LOG_UPDATE_STATUS_URI_PATTERN, commitLogId),
                                  new Gson().toJson(statusUpdateDto));
    }

    private long getEntityNextSequence(String entityName) {
        String entityNextSequenceUri = String.format(InterceptorConstants.NEXT_SEQUENCE_URI_PATTERN_, entityName);
        SequenceDto sequenceDto = requestSender.requestSequence(nodesInfo.getCurrentNode().getAddress(),
                                                                entityNextSequenceUri);
        return sequenceDto.getValue();
    }
}
