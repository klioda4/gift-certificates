package ru.clevertec.ecl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.projection.TagOfUser;

public interface TagService {

    Page<Tag> findAll(Pageable pageable);

    Tag findByName(String name);

    Tag findById(long id);

    TagOfUser findMostValuableTag();

    Tag create(TagCreateDto newTagDto);

    Tag updateById(long id, TagPutDto putDto);

    void deleteById(long id);
}
