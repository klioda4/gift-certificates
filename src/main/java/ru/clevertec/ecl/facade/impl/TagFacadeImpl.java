package ru.clevertec.ecl.facade.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.dto.response.TagOfUserDto;
import ru.clevertec.ecl.facade.TagFacade;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Component
public class TagFacadeImpl implements TagFacade {

    private final TagService tagService;
    private final TagDtoMapper tagMapper;

    @Override
    public Page<TagDto> findAll(Pageable pageable) {
        return tagService.findAll(pageable)
                   .map(tagMapper::mapToDto);
    }

    @Override
    public TagDto findById(long id) {
        return tagMapper.mapToDto(
            tagService.findById(id));
    }

    @Override
    public TagOfUserDto findMostValuableTag() {
        return tagMapper.mapToDto(
            tagService.findMostValuableTag());
    }

    @Override
    public TagDto create(TagCreateDto newTagDto) {
        return tagMapper.mapToDto(
            tagService.create(newTagDto));
    }

    @Override
    public TagDto updateById(long id, TagPutDto putDto) {
        return tagMapper.mapToDto(
            tagService.updateById(id, putDto));
    }

    @Override
    public void deleteById(long id) {
        tagService.deleteById(id);
    }
}
