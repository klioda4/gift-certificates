package ru.clevertec.ecl.cluster.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;

@UtilityClass
public class RequestCheckHelper {

    public boolean needToSkipHandling(HttpServletRequest request) {
        String needToSkipHandling = request.getHeader(InterceptorConstants.SKIP_HANDLING_ATTRIBUTE);
        return Optional.ofNullable(needToSkipHandling)
                   .map(Boolean::parseBoolean)
                   .orElse(false);
    }

    public boolean isInterceptorRequest(HttpServletRequest request) {
        Object isInterceptorRequest = request.getAttribute(InterceptorConstants.IS_INTERCEPTOR_REQUEST_ATTRIBUTE);
        return Boolean.TRUE.equals(isInterceptorRequest);
    }

    public Optional<Long> getCommitLogId(HttpServletRequest request) {
        Long commitLogId = (Long) request.getAttribute(InterceptorConstants.COMMIT_LOG_ID_ATTRIBUTE);
        return Optional.ofNullable(commitLogId);
    }
}
