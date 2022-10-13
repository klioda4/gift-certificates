package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.response.OrderDto;
import ru.clevertec.ecl.facade.OrderFacade;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.util.mapping.OrderDtoMapper;

@RequiredArgsConstructor
@Component
public class OrderFacadeImpl implements OrderFacade {

    private final OrderService orderService;
    private final OrderDtoMapper orderMapper;

    @Override
    public Page<OrderDto> findAllByUserId(long userId, Pageable pageable) {
        return orderService.findAllByUserId(userId, pageable)
            .map(orderMapper::mapToDto);
    }

    @Override
    public OrderDto findMostRecentByUserId(long userId) {
        return orderMapper.mapToDto(
            orderService.findMostRecentByUserId(userId));
    }

    @Override
    public OrderDto create(OrderCreateDto createDto) {
        return orderMapper.mapToDto(
            orderService.create(createDto));
    }

    @Override
    public void deleteById(long id) {
        orderService.deleteById(id);
    }
}
