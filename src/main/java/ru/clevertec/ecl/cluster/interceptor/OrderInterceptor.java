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
import ru.clevertec.ecl.cluster.handler.OrderRequestHandler;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.RequestCheckHelper;
import ru.clevertec.ecl.cluster.util.RequestPathParser;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderInterceptor implements HandlerInterceptor {

    private final OrderRequestHandler orderRequestHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {

        log.info("{} request to url {}", request.getMethod(), request.getRequestURL());
        if (RequestCheckHelper.needToSkipHandling(request)) {
            log.debug("Skip handling - request already handled");
            return true;
        }

        String servletPath = request.getServletPath();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        CachedHttpServletRequest cachedRequest = (CachedHttpServletRequest) request;
        if (httpMethod == HttpMethod.GET && RequestPathParser.isRequestById(servletPath)) {
            return orderRequestHandler.doGetById(cachedRequest, response);

        } else if ((httpMethod == HttpMethod.GET) && (RequestPathParser.isRequestToAll(servletPath)
                                                          || RequestPathParser.isFindAllOrdersByUserId(servletPath))) {
            orderRequestHandler.doGetAll(cachedRequest, response);
            return false;

        } else if ((httpMethod == HttpMethod.POST) && RequestPathParser.isRequestToAll(servletPath)) {
            return orderRequestHandler.doCreate(cachedRequest, response);

        } else if (((httpMethod == HttpMethod.PUT) || (httpMethod == HttpMethod.PATCH)
                        || (httpMethod == HttpMethod.DELETE)) && RequestPathParser.isRequestById(servletPath)) {
            return orderRequestHandler.doModifyById(cachedRequest, response);

        } else {
            throw new UnsupportedOperationException("Unknown operation to orders");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        RequestCheckHelper.getCommitLogId(request)
            .ifPresent(orderRequestHandler::markCommitLogAsApplied);
    }
}
