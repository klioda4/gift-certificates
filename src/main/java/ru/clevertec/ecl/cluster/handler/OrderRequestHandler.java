package ru.clevertec.ecl.cluster.handler;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

public interface OrderRequestHandler {

    boolean doGetById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException;

    void doGetAll(HttpServletRequest request, HttpServletResponse response) throws IOException;

    boolean doCreate(CachedHttpServletRequest request, HttpServletResponse response) throws IOException;

    boolean doModifyById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException;

    void markCommitLogAsApplied(long commitLogId);
}
