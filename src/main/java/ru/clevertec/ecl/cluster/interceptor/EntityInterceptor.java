package ru.clevertec.ecl.cluster.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.handler.EntityRequestHandler;
import ru.clevertec.ecl.cluster.util.RequestCheckHelper;
import ru.clevertec.ecl.cluster.util.requestQualify.EntityRequestTypeQualifier;
import ru.clevertec.ecl.cluster.util.requestQualify.EntityRequestTypeQualifier.EntityRequestType;

@RequiredArgsConstructor
@Slf4j
@Component
public class EntityInterceptor implements HandlerInterceptor {

    private final EntityRequestHandler entityRequestHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("{} request to url {}", request.getMethod(), request.getRequestURL());
        if (RequestCheckHelper.isForwarded(request)) {
            log.debug("Request is forwarded - skip handling");
            return true;
        }

        EntityRequestType entityRequestType = EntityRequestTypeQualifier.getRequestType(request);
        switch (entityRequestType) {
            case GET:
                log.debug("Accept GET request");
                return true;

            case CHANGE_DATA:
            case CREATE:
                request.setAttribute(InterceptorConstants.NEED_TO_DISTRIBUTE_ATTRIBUTE, true);
                log.debug("Accept CREATE request");
                return true;

            default:
                throw new UnsupportedOperationException("Unknown operation to entities");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        if (RequestCheckHelper.doesNeedToDistribute(request)) {
            log.debug("Distribute request to all nodes");
            CachedHttpServletRequest cachedRequest = (CachedHttpServletRequest) request;
            entityRequestHandler.distributeRequest(cachedRequest, response);
        }
    }
}
