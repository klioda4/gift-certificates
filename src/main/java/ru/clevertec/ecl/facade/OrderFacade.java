package ru.clevertec.ecl.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.response.OrderDto;

public interface OrderFacade {

    Page<OrderDto> findAllByUserId(long userId, Pageable pageable);

    OrderDto findMostRecentByUserId(long userId);

    OrderDto create(OrderCreateDto createDto);

    void deleteById(long id);
}
