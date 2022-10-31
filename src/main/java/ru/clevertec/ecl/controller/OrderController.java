package ru.clevertec.ecl.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
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
    public Page<OrderDto> findAllByUserId(@PathVariable @Positive long userId,
                                          Pageable pageable) {
        log.info("GET request to /v1/orders/user/{userId} with params: userId = {}, pageable = {}", userId, pageable);
        return orderFacade.findAllByUserId(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody @Valid OrderCreateDto createDto) {
        return orderFacade.create(createDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Positive long id) {
        orderFacade.deleteById(id);
    }
}
