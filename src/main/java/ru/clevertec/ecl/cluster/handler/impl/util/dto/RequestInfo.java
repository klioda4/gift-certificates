package ru.clevertec.ecl.cluster.handler.impl.util.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpMethod;

@Value
@Builder(toBuilder = true)
public class RequestInfo {

    HttpMethod httpMethod;
    String servletPath;
    String body;
}
