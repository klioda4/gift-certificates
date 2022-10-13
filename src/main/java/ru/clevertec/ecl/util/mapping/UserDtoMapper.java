package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.model.User;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserDtoMapper {

    UserDto mapToDto(User user);
}
