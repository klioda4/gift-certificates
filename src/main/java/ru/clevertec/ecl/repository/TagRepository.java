package ru.clevertec.ecl.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);
}
