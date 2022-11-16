package ru.clevertec.ecl.cluster.util.sender;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;
import ru.clevertec.ecl.dto.response.HealthCheckInfo;
import ru.clevertec.ecl.dto.response.SequenceDto;

public interface RequestSender {

    ResponseEntity<List<Object>> forwardGetAll(HttpServletRequest request, String host);

    ResponseEntity<String> forwardRequest(CachedHttpServletRequest request, String host, boolean skipHandling);

    ResponseEntity<String> sendRequest(HttpMethod method, String host, String uri, String body);

    SequenceDto requestSequence(String host, String uri);

    HealthCheckInfo checkHealth(String httpAddress);
}
