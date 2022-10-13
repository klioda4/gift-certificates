package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.model.Order;

public interface OrderService {

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    Order findMostRecentByUserId(long userId);

    Order create(OrderCreateDto createDto);

    void deleteById(long id);
}
