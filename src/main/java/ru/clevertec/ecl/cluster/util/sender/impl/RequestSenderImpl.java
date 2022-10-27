package ru.clevertec.ecl.cluster.util.sender.impl;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.util.constant.AppConstants;

@Slf4j
@Component
public class RequestSenderImpl implements RequestSender {

    @Override
    public ResponseEntity<List<Object>> forwardGetAll(HttpServletRequest request, Node nodeToForward) {
        log.debug("Forward request to {}", nodeToForward);
        return getWebClient(nodeToForward.getAddress())
            .get()
            .uri(getUriWithParameters(request))
            .retrieve()
            .toEntityList(Object.class)
            .block();
    }

    @Override
    public ResponseEntity<String> forwardRequest(CachedHttpServletRequest request, Node nodeToForward) {
        log.debug("Forward request to {}", nodeToForward);
        return (request.getBody() == null)
               ? forwardRequestWithoutBody(request, nodeToForward)
               : forwardRequestWithBody(request, nodeToForward);
    }

    private ResponseEntity<String> forwardRequestWithBody(CachedHttpServletRequest request, Node nodeToForward) {
        return getWebClient(nodeToForward.getAddress())
            .method(HttpMethod.valueOf(request.getMethod()))
            .uri(getUriWithParameters(request))
            .bodyValue(request.getBody())
            .retrieve()
            .toEntity(String.class)
            .block();
    }

    private ResponseEntity<String> forwardRequestWithoutBody(HttpServletRequest request, Node nodeToForward) {
        return getWebClient(nodeToForward.getAddress())
            .method(HttpMethod.valueOf(request.getMethod()))
            .uri(getUriWithParameters(request))
            .retrieve()
            .toEntity(String.class)
            .block();
    }

    protected final WebClient getWebClient(String baseUrl) {
        return WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_ENCODING, AppConstants.ENCODING)
            .defaultHeader(InterceptorConstants.FORWARDED_ATTRIBUTE, Boolean.toString(true))
            .build();
    }

    protected final String getUriWithParameters(HttpServletRequest request) {
        StringBuilder requestPath = new StringBuilder(request.getRequestURI());
        if (request.getQueryString() != null) {
            requestPath
                .append('?')
                .append(request.getQueryString());
        }
        return requestPath.toString();
    }
}
