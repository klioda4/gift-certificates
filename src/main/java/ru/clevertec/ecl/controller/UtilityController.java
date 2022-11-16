package ru.clevertec.ecl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.response.HealthCheckInfo;

@RestController
@RequestMapping("/v1/util")
public class UtilityController {

    @GetMapping("/health")
    public ResponseEntity<HealthCheckInfo> getHealthCheck() {
        return ResponseEntity.ok(new HealthCheckInfo(true));
    }
}
