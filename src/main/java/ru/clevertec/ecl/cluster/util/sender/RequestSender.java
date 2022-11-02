package ru.clevertec.ecl.cluster.util.sender;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import ru.clevertec.ecl.cluster.nodeInfo.Node;
import ru.clevertec.ecl.cluster.request.CachedHttpServletRequest;

public interface RequestSender {

    ResponseEntity<List<Object>> forwardGetAll(HttpServletRequest request, Node nodeToForward);

    ResponseEntity<String> forwardRequest(CachedHttpServletRequest request, Node nodeToForward);
}
