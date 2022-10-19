package ru.clevertec.ecl.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.dto.response.TagOfUserDto;

public interface TagFacade {

    Page<TagDto> findAll(Pageable pageable);

    TagDto findById(long id);

    TagOfUserDto findMostValuableTag();

    TagDto create(TagPutDto newTagDto);

    TagDto updateById(long id, TagPutDto putDto);

    void deleteById(long id);
}
