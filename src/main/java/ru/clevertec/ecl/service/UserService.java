package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.model.User;

public interface UserService {

    Page<User> findAll(Pageable pageable);

    User findById(long id);
}
