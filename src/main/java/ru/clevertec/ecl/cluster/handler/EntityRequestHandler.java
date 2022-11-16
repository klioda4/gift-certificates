package ru.clevertec.ecl.cluster.handler;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

public interface EntityRequestHandler {

    void doCreate(CachedHttpServletRequest request, HttpServletResponse response) throws IOException;

    void doModifyingById(CachedHttpServletRequest request, HttpServletResponse response) throws IOException;

    void markCommitLogAsApplied(long commitLogId);
}
