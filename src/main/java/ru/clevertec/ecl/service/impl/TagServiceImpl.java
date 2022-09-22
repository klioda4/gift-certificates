package ru.clevertec.ecl.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.util.mapping.DtoMapper;

@Service
public class TagServiceImpl extends CrudServiceImpl<Tag, TagDto, TagCreateDto, Long> {

    public TagServiceImpl(
        JpaRepository<Tag, Long> repository,
        DtoMapper<Tag, TagDto, TagCreateDto> mapper) {

        super(repository, mapper);
    }
}
