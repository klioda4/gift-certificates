package ru.clevertec.ecl.cluster.handler;

import javax.servlet.http.HttpServletResponse;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

public interface EntityRequestHandler {

    void distributeRequest(CachedHttpServletRequest request, HttpServletResponse response);
}
