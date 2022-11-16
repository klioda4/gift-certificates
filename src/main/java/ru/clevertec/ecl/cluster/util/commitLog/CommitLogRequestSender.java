package ru.clevertec.ecl.cluster.util.commitLog;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.util.mapping.custom.EntityNameMapper;

@RequiredArgsConstructor
@Component
public class CommitLogRequestSender {

    private static final String CREATE_URI_PATH = "/api/v1/%s";
    private static final String MODIFY_BY_ID_URI_PATH = "/api/v1/%s/%d";

    private final RequestSender requestSender;
    private final NodesInfo nodesInfo;
    private final EntityNameMapper entityNameMapper;

    public void applyCommitLog(CommitLogDto commitLogDto) {
        String entityNameInPath = entityNameMapper.mapEntityNameToControllerBindingName(commitLogDto.getEntityName());
        String uri = (HttpMethod.POST == HttpMethod.valueOf(commitLogDto.getHttpMethod()))
                     ? String.format(CREATE_URI_PATH, entityNameInPath)
                     : String.format(MODIFY_BY_ID_URI_PATH, entityNameInPath, commitLogDto.getEntityId());

        requestSender.sendRequest(HttpMethod.valueOf(commitLogDto.getHttpMethod()),
                                  nodesInfo.getCurrentNode().getAddress(),
                                  uri,
                                  commitLogDto.getBody());
    }
}
