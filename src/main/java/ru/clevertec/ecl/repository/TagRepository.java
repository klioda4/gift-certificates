package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
