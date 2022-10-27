package ru.clevertec.ecl.facade;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.request.OrderPutDto;
import ru.clevertec.ecl.dto.response.OrderDto;

public interface OrderFacade {

    List<OrderDto> findAll();

    Page<OrderDto> findAllByUserId(long userId, Pageable pageable);

    OrderDto findById(long id);

    OrderDto create(OrderCreateDto createDto);

    OrderDto updateById(long id, OrderPutDto putDto);

    void deleteById(long id);
}
