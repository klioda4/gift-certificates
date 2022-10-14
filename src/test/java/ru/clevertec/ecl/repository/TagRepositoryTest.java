package ru.clevertec.ecl.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.model.Tag;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@DataJpaTest
@ActiveProfiles("test")
class TagRepositoryTest {

    private final TagRepository tagRepository;

    @Test
    void givenName_whenFindByName_thenReturnExpectedTag() {
        String givenName = "halloween";

        Optional<Tag> actualTag = tagRepository.findByName(givenName);

        Tag expectedTag = new Tag(1L, "halloween");
        assertTrue(actualTag.isPresent(), "Tag with given name is not found");
        assertEquals(expectedTag, actualTag.get());
    }
}
