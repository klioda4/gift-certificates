package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.model.User;

@Mapper
public interface UserDtoMapper {

    UserDto mapToDto(User user);
}
