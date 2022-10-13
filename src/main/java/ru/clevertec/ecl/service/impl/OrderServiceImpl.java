package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.Order_;
import ru.clevertec.ecl.model.User;
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
    public Page<Order> findAllByUserId(long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Order findMostRecentByUserId(long userId) {
        return orderRepository.findFirstByUserIdOrderByPurchaseDateDesc(userId)
            .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NAME, "user.id", userId));
    }

    @Override
    @Transactional
    public Order create(OrderCreateDto createDto) {
        User user = userService.findById(createDto.getUserId());
        GiftCertificate certificateOfOrder = certificateService.findByIdLazy(createDto.getGiftCertificateId());
        return orderRepository.save(
            orderMapper.createOrder(user, certificateOfOrder));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        orderRepository.delete(
            orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_ENTITY_NAME, Order_.ID, id)));
    }
}
