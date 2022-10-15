package ru.clevertec.ecl.test.stub;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

@UtilityClass
public class StubHelper {

    public <T, ID> void stubRepositorySave(JpaRepository<T, ID> repositoryMockito) {
        when(repositoryMockito.save(any()))
            .then(returnsFirstArg());
    }

    public <T, ID> void stubRepositorySaveAndFlush(JpaRepository<T, ID> repositoryMockito) {
        when(repositoryMockito.saveAndFlush(any()))
            .then(returnsFirstArg());
    }
}
