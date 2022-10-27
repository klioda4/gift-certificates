package ru.clevertec.ecl.cluster.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.handler.OrderRequestHandler;
import ru.clevertec.ecl.cluster.util.RequestCheckHelper;
import ru.clevertec.ecl.cluster.util.requestQualify.OrderRequestTypeQualifier;
import ru.clevertec.ecl.cluster.util.requestQualify.OrderRequestTypeQualifier.OrderRequestType;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderInterceptor implements HandlerInterceptor {

    private final OrderRequestHandler orderRequestHandler;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {

        log.info("{} request to url {}", request.getMethod(), request.getRequestURL());
        if (RequestCheckHelper.isForwarded(request)) {
            log.debug("Skip handling - request already handled");
            return true;
        }

        CachedHttpServletRequest cachedRequest = (CachedHttpServletRequest) request;
        OrderRequestType orderRequestType = OrderRequestTypeQualifier.getRequestType(request);
        log.debug("Request is {}", orderRequestType);
        switch (orderRequestType) {
            case GET_BY_ID:
                return orderRequestHandler.doGetById(cachedRequest, response);

            case GET_ALL:
                orderRequestHandler.doGetAll(cachedRequest, response);
                return false;

            case UPDATE_BY_ID:
                return orderRequestHandler.doUpdateById(cachedRequest, response);

            case CREATE:
                return orderRequestHandler.doCreate(cachedRequest, response);

            case DELETE:
                return orderRequestHandler.doDelete(cachedRequest, response);

            default:
                throw new UnsupportedOperationException("Unknown operation to orders");
        }
    }
}
