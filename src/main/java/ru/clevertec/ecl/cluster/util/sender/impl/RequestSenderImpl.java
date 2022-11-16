package ru.clevertec.ecl.cluster.util.sender.impl;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.cluster.util.sender.RequestSender;
import ru.clevertec.ecl.dto.response.HealthCheckInfo;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.util.constant.AppConstants;

@Slf4j
@Component
public class RequestSenderImpl implements RequestSender {

    @Override
    public ResponseEntity<List<Object>> forwardGetAll(HttpServletRequest request, String httpAddress) {
        log.debug("Forward request to {}", httpAddress);
        return getWebClient(httpAddress)
                   .get()
                   .uri(getUriWithParameters(request))
                   .header(InterceptorConstants.SKIP_HANDLING_ATTRIBUTE, Boolean.toString(true))
                   .retrieve()
                   .toEntityList(Object.class)
                   .block();
    }

    @Override
    public ResponseEntity<String> forwardRequest(CachedHttpServletRequest request, String httpAddress,
                                                 boolean skipHandling) {
        log.debug("Forward request to {}", httpAddress);
        return (request.getBody() == null)
               ? forwardRequestWithoutBody(httpAddress,
                                           HttpMethod.valueOf(request.getMethod()),
                                           getUriWithParameters(request),
                                           skipHandling)
               : forwardRequestWithBody(httpAddress,
                                        HttpMethod.valueOf(request.getMethod()),
                                        getUriWithParameters(request),
                                        request.getBody(),
                                        skipHandling);
    }

    @Override
    public ResponseEntity<String> sendRequest(HttpMethod method, String host, String uri, String body) {
        if (body == null) {
            return sendRequestWithoutBody(method, host, uri);
        } else {
            return sendRequestWithBody(method, host, uri, body);
        }
    }

    @Override
    public SequenceDto requestSequence(String host, String uri) {
        ResponseEntity<SequenceDto> sequenceResponseEntity = getWebClient(host)
                                                                 .get()
                                                                 .uri(uri)
                                                                 .retrieve()
                                                                 .toEntity(SequenceDto.class)
                                                                 .block();
        return Optional.ofNullable(sequenceResponseEntity)
                   .map(HttpEntity::getBody)
                   .orElseThrow(() -> new IllegalStateException("Sequence isn't acquired"));
    }

    @Override
    public HealthCheckInfo checkHealth(String httpAddress) {
        ResponseEntity<HealthCheckInfo> healthCheckResponse = null;
        try {
            healthCheckResponse = WebClient.create(httpAddress)
                                      .get()
                                      .uri(AppConstants.HEALTH_CHECK_ENDPOINT_URI)
                                      .retrieve()
                                      .toEntity(HealthCheckInfo.class)
                                      .block();
        } catch (WebClientRequestException | WebClientResponseException e) {
            log.debug("Health check is failed for node {} with exception: {}", httpAddress, e.getMessage());
        }
        return Optional.ofNullable(healthCheckResponse)
                   .map(HttpEntity::getBody)
                   .orElseGet(() -> new HealthCheckInfo(false));
    }

    public ResponseEntity<String> sendRequestWithBody(HttpMethod method, String host, String uri, String body) {
        return forwardRequestWithBody(host, method, uri, body, true);
    }

    private ResponseEntity<String> sendRequestWithoutBody(HttpMethod method, String host, String uri) {
        return forwardRequestWithoutBody(host, method, uri, true);
    }

    private ResponseEntity<String> forwardRequestWithoutBody(String address, HttpMethod httpMethod, String uri,
                                                             boolean skipHandling) {
        return getWebClient(address)
                   .method(httpMethod)
                   .uri(uri)
                   .header(InterceptorConstants.SKIP_HANDLING_ATTRIBUTE, Boolean.toString(skipHandling))
                   .retrieve()
                   .toEntity(String.class)
                   .log()
                   .block();
    }

    private ResponseEntity<String> forwardRequestWithBody(String address, HttpMethod httpMethod, String uri,
                                                          String body, boolean skipHandling) {
        return getWebClient(address)
                   .method(httpMethod)
                   .uri(uri)
                   .header(InterceptorConstants.SKIP_HANDLING_ATTRIBUTE, Boolean.toString(skipHandling))
                   .bodyValue(body)
                   .retrieve()
                   .toEntity(String.class)
                   .log()
                   .block();
    }

    private WebClient getWebClient(String baseUrl) {
        return WebClient.builder()
                   .baseUrl(baseUrl)
                   .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                   .defaultHeader(HttpHeaders.CONTENT_ENCODING, AppConstants.ENCODING)
                   .defaultHeader(InterceptorConstants.IS_INTERCEPTOR_REQUEST_ATTRIBUTE, Boolean.TRUE.toString())
                   .build();
    }

    private String getUriWithParameters(HttpServletRequest request) {
        StringBuilder requestPath = new StringBuilder(request.getRequestURI());
        if (request.getQueryString() != null) {
            requestPath
                .append('?')
                .append(request.getQueryString());
        }
        return requestPath.toString();
    }
}
