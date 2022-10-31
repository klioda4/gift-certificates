package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.Order_;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = Order_.GIFT_CERTIFICATE)
    Page<Order> findAllByUserId(long userId, Pageable pageable);
}
