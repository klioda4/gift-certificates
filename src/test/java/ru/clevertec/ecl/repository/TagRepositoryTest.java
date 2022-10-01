package ru.clevertec.ecl.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.clevertec.ecl.model.Tag;

@Sql({"classpath:db/sql/create schema.sql", "classpath:db/sql/fill tables.sql"})
@DataJpaTest
@ActiveProfiles("test")
class TagRepositoryTest {

    @Autowired
    private TagRepository repository;

    @Test
    void findByName() {
        String givenName = "halloween";

        Optional<Tag> resultTag = repository.findByName(givenName);

        Tag expected = new Tag(1L, "halloween");
        assertTrue(resultTag.isPresent(), "Tag with given name is not found");
        assertEquals(expected, resultTag.get());
    }
}