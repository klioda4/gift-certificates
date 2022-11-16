package ru.clevertec.ecl.controller.advice;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.nodeInfo.NodesInfo;
import ru.clevertec.ecl.cluster.util.RequestPathParser;
import ru.clevertec.ecl.dto.response.ExceptionResponse;
import ru.clevertec.ecl.util.constant.ErrorDescription;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Order(0)
public class WebClientExceptionHandler {

    private final NodesInfo nodesInfo;

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<ExceptionResponse> handleWebClientRequestException(
        WebClientRequestException e,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {

        log.warn(e.getMessage());
        String requestNodeAddress = RequestPathParser.getServerAddressFromUrl(e.getUri().toString());
        boolean statusChanged = markNodeAsInactive(requestNodeAddress);
        if (statusChanged) {
            sendSelfRedirect(request, response);
            return null;
        } else {
            ErrorDescription errorDescription = ErrorDescription.NODE_CONNECTION_ERROR;
            return getResponseEntity(HttpStatus.valueOf(errorDescription.getStatusCode()),
                                     errorDescription.getErrorCode(),
                                     errorDescription.getDefaultMessage());
        }
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, int errorCode, String errorMessage) {
        return new ResponseEntity<>(ExceptionResponse.builder()
                                        .status(status.value())
                                        .errorCode(errorCode)
                                        .errorMessage(errorMessage)
                                        .build(),
                                    status);
    }

    private boolean markNodeAsInactive(String nodeAddress) {
        Optional<Node> inactiveNode = nodesInfo.getShards().stream()
                                          .flatMap(Collection::stream)
                                          .filter(node -> node.getAddress().equals(nodeAddress))
                                          .findFirst();
        if (inactiveNode.isPresent()) {
            boolean statusChanged = inactiveNode.get().isActive();
            inactiveNode.get().setActive(false);
            log.info("Node {} marked as inactive", nodeAddress);
            return statusChanged;
        } else {
            log.error("Node {} not found, but should be present", nodeAddress);
            return false;
        }
    }

    private void sendSelfRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(String.format("%s?%s", request.getRequestURL(), request.getContextPath()));
    }
}
