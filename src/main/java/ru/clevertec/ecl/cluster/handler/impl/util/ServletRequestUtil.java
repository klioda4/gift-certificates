package ru.clevertec.ecl.cluster.handler.impl.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;
import ru.clevertec.ecl.cluster.handler.impl.util.dto.RequestInfo;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

@UtilityClass
public class ServletRequestUtil {

    public RequestInfo buildRequestInfo(CachedHttpServletRequest request) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        String servletPath = request.getServletPath();
        String body = request.getBody();
        return RequestInfo.builder()
                   .httpMethod(httpMethod)
                   .servletPath(servletPath)
                   .body(body)
                   .build();
    }
}
