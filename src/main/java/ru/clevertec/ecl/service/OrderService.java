package ru.clevertec.ecl.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.request.OrderPutDto;
import ru.clevertec.ecl.model.Order;

public interface OrderService {

    List<Order> findAll();

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    Order findById(long id);

    Order create(OrderCreateDto createDto);

    Order updateById(long id, OrderPutDto putDto);

    void deleteById(long id);
}
