package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;

public interface TagService {

    Page<Tag> findAll(Pageable pageable);

    Tag findOrCreateByName(String name);

    Tag findById(long id);

    Tag create(TagPutDto newTagDto);

    Tag updateById(long id, TagPutDto putDto);

    void deleteById(long id);
}
