package ru.clevertec.ecl.cluster.interceptor;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.calculation.ShardIdCalculator;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.util.enums.CommitLogStatus;

@RequiredArgsConstructor
@Slf4j
@Component
public class CommitLogFilteringInterceptor implements HandlerInterceptor {

    private static final String ORDER_ENTITY_NAME = Order.class.getSimpleName();

    private final ShardIdCalculator shardIdCalculator;
    private final NodesInfo nodesInfo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        if (httpMethod == HttpMethod.POST) {
            CachedHttpServletRequest cachedRequest = (CachedHttpServletRequest) request;
            Gson gson = new Gson();
            CommitLogDto commitLogDto = gson.fromJson(cachedRequest.getBody(), CommitLogDto.class);
            if (ORDER_ENTITY_NAME.equals(commitLogDto.getEntityName())) {
                int destinationShardIndex = shardIdCalculator.calculateShardIndexById(commitLogDto.getEntityId());
                if (destinationShardIndex != nodesInfo.getCurrentShardIndex()) {
                    CommitLogDto ignoredCommitLog = commitLogDto.toBuilder()
                                                        .status(CommitLogStatus.IGNORED)
                                                        .build();
                    cachedRequest.setBody(gson.toJson(ignoredCommitLog));
                }
            }
        }
        return true;
    }
}
