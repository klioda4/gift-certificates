package ru.clevertec.ecl.util.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.test.supply.UserDataSupplier;

class UserDtoMapperTest {

    UserDtoMapper userMapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void givenUser_whenMapToDto_thenReturnExpectedUserDto() {
        User givenUser = UserDataSupplier.getUser();

        UserDto actualUserDto = userMapper.mapToDto(givenUser);

        UserDto expectedUserDto = UserDataSupplier.getUserDto();
        assertEquals(expectedUserDto, actualUserDto);
    }
}
