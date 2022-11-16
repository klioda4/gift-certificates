package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.facade.UserFacade;
import ru.clevertec.ecl.service.UserService;
import ru.clevertec.ecl.util.mapping.UserDtoMapper;

@RequiredArgsConstructor
@Component
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserDtoMapper userMapper;

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userService.findAll(pageable)
                   .map(userMapper::mapToDto);
    }

    @Override
    public UserDto findById(long id) {
        return userMapper.mapToDto(
            userService.findById(id));
    }
}
