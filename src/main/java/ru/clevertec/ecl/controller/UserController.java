package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.response.UserDto;
import ru.clevertec.ecl.facade.UserFacade;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping
    public Page<UserDto> findAll(Pageable pageable) {
        log.info("GET request to /v1/users with params: pageable = {}", pageable);
        return userFacade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable long id) {
        return userFacade.findById(id);
    }
}
