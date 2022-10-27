package ru.clevertec.ecl.cluster.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

@Slf4j
@Component
public class RequestWrapFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        CachedHttpServletRequest requestWrapper = new CachedHttpServletRequest(request);
        filterChain.doFilter(requestWrapper, response);
    }
}
