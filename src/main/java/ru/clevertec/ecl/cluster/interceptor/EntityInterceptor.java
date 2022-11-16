package ru.clevertec.ecl.cluster.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.clevertec.ecl.cluster.handler.EntityRequestHandler;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.RequestCheckHelper;
import ru.clevertec.ecl.cluster.util.RequestPathParser;

@RequiredArgsConstructor
@Slf4j
@Component
public class EntityInterceptor implements HandlerInterceptor {

    private final EntityRequestHandler entityRequestHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {

        log.info("{} request to url {}", request.getMethod(), request.getRequestURL());
        if (RequestCheckHelper.needToSkipHandling(request)) {
            log.debug("Request is forwarded - skip handling");
            return true;
        }

        String servletPath = request.getServletPath();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        CachedHttpServletRequest cachedRequest = (CachedHttpServletRequest) request;
        if (httpMethod == HttpMethod.GET) {
            log.debug("Accept GET request");
            return true;

        } else if ((httpMethod == HttpMethod.PUT) || (httpMethod == HttpMethod.PATCH)
                       || (httpMethod == HttpMethod.DELETE)) {
            entityRequestHandler.doModifyingById(cachedRequest, response);
            return true;

        } else if (httpMethod == HttpMethod.POST && RequestPathParser.isRequestToAll(servletPath)) {
            entityRequestHandler.doCreate(cachedRequest, response);
            return true;

        } else {
            throw new UnsupportedOperationException("Unknown operation to entities");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        RequestCheckHelper.getCommitLogId(request)
            .ifPresent(entityRequestHandler::markCommitLogAsApplied);
    }
}
