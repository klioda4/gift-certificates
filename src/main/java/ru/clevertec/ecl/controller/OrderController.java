package ru.clevertec.ecl.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.request.OrderPutDto;
import ru.clevertec.ecl.dto.response.OrderDto;
import ru.clevertec.ecl.facade.OrderFacade;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/orders")
@Validated
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderDto>> findAllByUserId(@PathVariable @Positive long userId,
                                                          Pageable pageable) {
        log.info("GET request to /v1/orders/user/{userId} with userId = {}, pageable = {}", userId, pageable);
        Page<OrderDto> ordersPage = orderFacade.findAllByUserId(userId, pageable);
        log.info("Result: {}", ordersPage);
        return ResponseEntity.ok(ordersPage);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {
        log.info("GET request to /v1/orders");
        List<OrderDto> orders = orderFacade.findAll();
        log.info("Result: {}", orders);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable @Positive long id) {
        log.info("GET request to /v1/orders/{id} with id = {}", id);
        OrderDto order = orderFacade.findById(id);
        log.info("Result: {}", order);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderCreateDto createDto) {
        log.info("POST request to /v1/orders with createDto = {}", createDto);
        OrderDto order = orderFacade.create(createDto);
        log.info("Result: {}", order);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateById(@PathVariable @Positive long id,
                                               @RequestBody OrderPutDto putDto) {
        log.info("POST request to /v1/orders with id = {}", id);
        OrderDto order = orderFacade.updateById(id, putDto);
        log.info("Result: {}", order);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable @Positive long id) {
        log.info("DELETE request to /v1/orders/{id} with id = {}", id);
        orderFacade.deleteById(id);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }
}
