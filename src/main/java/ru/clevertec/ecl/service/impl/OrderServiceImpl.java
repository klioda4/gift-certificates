package ru.clevertec.ecl.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.dto.request.OrderPutDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.Order_;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.model.entityGraph.EntityGraphNames;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.OrderService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.mapping.OrderDtoMapper;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_ENTITY_NAME = "Order";

    private final OrderRepository orderRepository;
    private final OrderDtoMapper orderMapper;
    private final UserService userService;
    private final GiftCertificateService certificateService;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll(Pageable.unpaged(),
                                       NamedEntityGraph.fetching(EntityGraphNames.ORDER_WITH_CERTIFICATE))
                   .toList();
    }

    @Override
    public Page<Order> findAllByUserId(long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Order findById(long id) {
        return orderRepository.findById(id, NamedEntityGraph.fetching(EntityGraphNames.ORDER_WITH_CERTIFICATE))
                   .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NAME, Order_.ID, id));
    }

    @Override
    @Transactional
    public Order create(OrderCreateDto createDto) {
        User user = userService.findById(createDto.getUserId());
        GiftCertificate certificateOfOrder = certificateService.findByIdLazy(createDto.getGiftCertificateId());
        return orderRepository.save(
            orderMapper.mapToOrder(createDto, user, certificateOfOrder));
    }

    @Override
    public Order updateById(long id, OrderPutDto orderPutDto) {
        Order existingOrder = findById(id);
        orderMapper.updateOrder(existingOrder, orderPutDto);
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Order order = orderRepository.findById(id)
                          .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NAME, Order_.ID, id));
        orderRepository.delete(order);
    }
}
