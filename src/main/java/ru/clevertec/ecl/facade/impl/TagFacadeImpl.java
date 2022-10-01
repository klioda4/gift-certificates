package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.facade.TagFacade;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Component
public class TagFacadeImpl implements TagFacade {

    private final TagService service;
    private final TagDtoMapper mapper;

    @Override
    public Page<TagDto> findAll(Pageable pageable) {
        return service.findAll(pageable)
            .map(mapper::mapToDto);
    }

    @Override
    public TagDto findById(long id) {
        return mapper.mapToDto(
            service.findById(id));
    }

    @Override
    public TagDto create(TagPutDto newTagDto) {
        return mapper.mapToDto(
            service.create(newTagDto));
    }

    @Override
    public TagDto replaceById(long id, TagPutDto newTagDto) {
        return mapper.mapToDto(
            service.replaceById(id, newTagDto));
    }

    @Override
    public void deleteById(long id) {
        service.deleteById(id);
    }
}
