package ru.clevertec.ecl.service;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagRequestDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.Tag;

public interface TagService {

    Page<Tag> findAll(Pageable pageable);

    Tag findOrCreateByName(String name);

    Tag findById(long id) throws ObjectNotFoundException;

    Tag create(TagRequestDto createDto);

    Tag updateById(long id, Map<String, Object> newFieldValues) throws ObjectNotFoundException;

    void deleteById(long id) throws ObjectNotFoundException;
}
