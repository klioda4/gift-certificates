package ru.clevertec.ecl.facade.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.request.OrderPutDto;
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
    public List<OrderDto> findAll() {
        return orderMapper.mapToDto(
            orderService.findAll());
    }

    @Override
    public Page<OrderDto> findAllByUserId(long userId, Pageable pageable) {
        return orderService.findAllByUserId(userId, pageable)
                   .map(orderMapper::mapToDto);
    }

    @Override
    public OrderDto findById(long id) {
        return orderMapper.mapToDto(
            orderService.findById(id));
    }

    @Override
    public OrderDto create(OrderCreateDto createDto) {
        return orderMapper.mapToDto(
            orderService.create(createDto));
    }

    @Override
    public OrderDto updateById(long id, OrderPutDto putDto) {
        return orderMapper.mapToDto(
            orderService.updateById(id, putDto));
    }

    @Override
    public void deleteById(long id) {
        orderService.deleteById(id);
    }
}
