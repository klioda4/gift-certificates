package ru.clevertec.ecl.cluster.util.requestQualify;

import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;
import ru.clevertec.ecl.cluster.util.RequestPathParser;

@UtilityClass
public class OrderRequestTypeQualifier {

    public OrderRequestType getRequestType(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        if (httpMethod == HttpMethod.GET && RequestPathParser.isRequestById(servletPath)) {
            return OrderRequestType.GET_BY_ID;

        } else if (((httpMethod == HttpMethod.PUT) || (httpMethod == HttpMethod.PATCH))
            && RequestPathParser.isRequestById(servletPath)) {
            return OrderRequestType.UPDATE_BY_ID;

        } else if ((httpMethod == HttpMethod.GET) && (RequestPathParser.isRequestToAll(servletPath)
            || RequestPathParser.isGetAllOrdersByUserId(servletPath))) {
            return OrderRequestType.GET_ALL;

        } else if ((httpMethod == HttpMethod.POST) && RequestPathParser.isRequestToAll(servletPath)) {
            return OrderRequestType.CREATE;

        } else if ((httpMethod == HttpMethod.DELETE) && RequestPathParser.isRequestById(servletPath)) {
            return OrderRequestType.DELETE;

        } else if ((httpMethod == HttpMethod.GET) && RequestPathParser.isGetAllOrdersByUserId(servletPath)) {
            return OrderRequestType.DELETE;

        } else {
            return OrderRequestType.OTHER_REQUEST;
        }
    }

    public enum OrderRequestType {

        GET_BY_ID,
        UPDATE_BY_ID,
        GET_ALL,
        CREATE,
        DELETE,
        OTHER_REQUEST
    }
}
