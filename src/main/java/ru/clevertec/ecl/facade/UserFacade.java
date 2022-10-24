package ru.clevertec.ecl.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.response.UserDto;

public interface UserFacade {

    Page<UserDto> findAll(Pageable pageable);

    UserDto findById(long id);
}
