package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.User;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.test.supply.UserDataSupplier;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void givenPageable_whenFindAll_thenReturnExpectedUserPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        Page<User> expectedUserPage = new PageImpl<>(getListOfSingleUser());
        when(userRepository.findAll(givenPageable))
            .thenReturn(expectedUserPage);

        Page<User> actualUserPage = userService.findAll(givenPageable);

        assertEquals(expectedUserPage, actualUserPage);
    }

    @Test
    void givenId_whenFindById_thenReturnExpectedUser() {
        long givenId = 1;
        when(userRepository.findById(givenId))
            .thenReturn(Optional.of(getUser()));

        User actualUser = userService.findById(givenId);

        User expectedUser = getUser();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void givenNotExistingId_whenFindById_thenThrowObjectNotFoundException() {
        long givenIncorrectId = 1;
        when(userRepository.findById(givenIncorrectId))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                     () -> userService.findById(givenIncorrectId));
    }

    private User getUser() {
        return UserDataSupplier.getUser();
    }

    public static List<User> getListOfSingleUser() {
        return UserDataSupplier.getListOfSingleUser();
    }
}
