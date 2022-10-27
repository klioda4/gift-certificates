package ru.clevertec.ecl.cluster.util.requestQualify;

import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;
import ru.clevertec.ecl.cluster.util.RequestPathParser;

@UtilityClass
public class EntityRequestTypeQualifier {

    public EntityRequestType getRequestType(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        if (httpMethod == HttpMethod.GET) {
            return EntityRequestType.GET;

        } else if (((httpMethod == HttpMethod.PUT) || (httpMethod == HttpMethod.PATCH))
                || (httpMethod == HttpMethod.DELETE)) {
            return EntityRequestType.CHANGE_DATA;

        } else if (httpMethod == HttpMethod.POST && RequestPathParser.isRequestToAll(servletPath)) {
            return EntityRequestType.CREATE;

        } else {
            return EntityRequestType.OTHER_REQUEST;
        }
    }

    public enum EntityRequestType {

        GET,
        CHANGE_DATA,
        CREATE,
        OTHER_REQUEST
    }
}
