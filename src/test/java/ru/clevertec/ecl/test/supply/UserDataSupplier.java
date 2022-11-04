package ru.clevertec.ecl.test.supply;

import java.util.Collections;
import java.util.List;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.model.User;

public class UserDataSupplier {

    public static final long DEFAULT_ID = 1L;
    public static final String DEFAULT_NAME = "default-user";

    public static User getUser() {
        return User.builder()
                   .id(DEFAULT_ID)
                   .name(DEFAULT_NAME)
                   .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                   .id(DEFAULT_ID)
                   .name(DEFAULT_NAME)
                   .build();
    }

    public static List<User> getListOfSingleUser() {
        return Collections.singletonList(getUser());
    }
}
