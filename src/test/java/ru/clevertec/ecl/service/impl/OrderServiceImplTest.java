package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.OrderCreateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.test.stub.StubHelper;
import ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier;
import ru.clevertec.ecl.test.supply.OrderDataSupplier;
import ru.clevertec.ecl.test.supply.UserDataSupplier;
import ru.clevertec.ecl.util.mapping.OrderDtoMapper;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDtoMapper orderMapper;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService certificateService;

    @Test
    void givenUserIdAndPageable_whenFindAllByUserId_thenReturnExpectedOrderPage() {
        long givenUserId = 1;
        Pageable givenPageable = Pageable.ofSize(20);
        when(orderRepository.findAllByUserId(givenUserId, givenPageable))
            .thenReturn(new PageImpl<>(getListOfSingleOrder()));

        Page<Order> resultOrderPage = orderRepository.findAllByUserId(givenUserId, givenPageable);

        List<Order> actualOrders = resultOrderPage.toList();
        List<Order> expectedOrders = getListOfSingleOrder();
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void givenCreateDto_whenCreate_thenReturnExpectedOrder() {
        OrderCreateDto orderCreateDto = getOrderCreateDto();
        StubHelper.stubRepositorySave(orderRepository);
        when(userService.findById(1L))
            .thenReturn(getUser());
        when(certificateService.findByIdLazy(1L))
            .thenReturn(getCertificate());
        when(orderMapper.createOrder(getUser(), getCertificate()))
            .thenReturn(getOrder());

        Order actualOrder = orderService.create(orderCreateDto);

        Order expectedOrder = getOrder();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void givenId_whenDeleteById_thenCallRepositoryDelete() {
        long givenId = 1;
        when(orderRepository.findById(givenId))
            .thenReturn(Optional.of(getOrder()));

        orderService.deleteById(givenId);

        verify(orderRepository)
            .delete(getOrder());
    }

    @Test
    void givenId_whenDeleteById_thenThrowEntityNotFoundException() {
        long givenId = 1;
        when(orderRepository.findById(givenId))
            .thenReturn(Optional.of(getOrder()));

        orderService.deleteById(givenId);

        verify(orderRepository)
            .delete(getOrder());
    }

    private Order getOrder() {
        return OrderDataSupplier.getOrder();
    }

    private OrderCreateDto getOrderCreateDto() {
        return OrderDataSupplier.getOrderCreateDto();
    }

    private User getUser() {
        return UserDataSupplier.getUser();
    }

    private GiftCertificate getCertificate() {
        return GiftCertificateDataSupplier.getGiftCertificate();
    }

    private List<Order> getListOfSingleOrder() {
        return Collections.singletonList(getOrder());
    }
}
