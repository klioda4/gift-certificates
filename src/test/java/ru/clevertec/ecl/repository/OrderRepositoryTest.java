package ru.clevertec.ecl.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.model.Order;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    private final OrderRepository orderRepository;

    @Test
    void givenUserId_whenFindAllByUserId_thenReturnExpectedNumberOfOrders() {
        long givenUserId = 1;

        Page<Order> actualOrderPage = orderRepository.findAllByUserId(givenUserId, Pageable.ofSize(20));

        int expectedNumberOfOrders = 2;
        assertEquals(expectedNumberOfOrders, actualOrderPage.getNumberOfElements());
    }

    @Test
    void givenUserId_whenFindFirstByUserIdOrderByPurchaseDateDesc_thenReturnOrderWithExpectedId() {
        long givenUserId = 1;

        Optional<Order> actualOrder = orderRepository.findFirstByUserIdOrderByPurchaseDateDesc(givenUserId);

        Long expectedOrderId = 2L;
        assertTrue(actualOrder.isPresent(), "Order is not found");
        assertEquals(expectedOrderId, actualOrder.get().getId(), "Id of the returned Order is incorrect");
    }
}
