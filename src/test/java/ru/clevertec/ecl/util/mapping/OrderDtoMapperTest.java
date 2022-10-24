package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.response.OrderDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier;
import ru.clevertec.ecl.test.supply.OrderDataSupplier;
import ru.clevertec.ecl.test.supply.UserDataSupplier;

class OrderDtoMapperTest {

    private final OrderDtoMapper orderMapper = Mappers.getMapper(OrderDtoMapper.class);

    @Test
    void givenOrder_whenMapToDto_thenReturnExpectedOrderDto() {
        Order givenOrder = OrderDataSupplier.getOrder();

        OrderDto actualOrderDto = orderMapper.mapToDto(givenOrder);

        OrderDto expectedOrderDto = OrderDataSupplier.getOrderDto();
        assertEquals(expectedOrderDto, actualOrderDto);
    }

    @Test
    void givenUserAndCertificate_whenCreateOrder_thenReturnExpectedOrder() {
        User givenUser = UserDataSupplier.getUser();
        GiftCertificate givenCertificate = GiftCertificateDataSupplier.getGiftCertificate();

        Order actualOrder = orderMapper.createOrder(givenUser, givenCertificate);

        Order expectedOrder = OrderDataSupplier.getOrderMappedFromCreateOrder(actualOrder.getPurchaseDate());
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void givenUserAndCertificate_whenCreateOrder_thenReturnOrderWithGivenReferences() {
        User givenUser = UserDataSupplier.getUser();
        GiftCertificate givenCertificate = GiftCertificateDataSupplier.getGiftCertificate();

        Order resultOrder = orderMapper.createOrder(givenUser, givenCertificate);

        User actualUser = resultOrder.getUser();
        GiftCertificate actualCertificate = resultOrder.getGiftCertificate();
        assertSame(givenUser, actualUser);
        assertSame(givenCertificate, actualCertificate);
    }
}
