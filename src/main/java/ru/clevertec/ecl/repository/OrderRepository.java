package ru.clevertec.ecl.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.Order_;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = Order_.GIFT_CERTIFICATE)
    Page<Order> findAllByUserId(long userId, Pageable pageable);

    /**
     * Returns the most recent order of specified user.
     *
     * @param userId id of user that owns searching order
     * @return the most recent order of specified user
     */
    @EntityGraph(attributePaths = Order_.GIFT_CERTIFICATE)
    Optional<Order> findFirstByUserIdOrderByPurchaseDateDesc(long userId);
}
