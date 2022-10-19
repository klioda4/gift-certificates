package ru.clevertec.ecl.util.mapping;

import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.dto.response.OrderDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Order;
import ru.clevertec.ecl.model.User;

@Mapper(imports = LocalDateTime.class)
public interface OrderDtoMapper {

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "cost", source = "order.cost")
    @Mapping(target = "duration", source = "order.duration")
    OrderDto mapToDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cost", source = "certificate.price")
    @Mapping(target = "giftCertificate", source = "certificate")
    @Mapping(target = "purchaseDate", expression = "java(LocalDateTime.now())")
    Order createOrder(User user, GiftCertificate certificate);
}
