package ru.clevertec.ecl.facade.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagRequestDto;
import ru.clevertec.ecl.facade.TagFacade;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Component
public class TagFacadeImpl implements TagFacade {

    private final TagService service;
    private final TagDtoMapper mapper;

    @Override
    public TagDto findById(long id) {
        return mapper.mapToDto(
            service.findById(id));
    }

    @Override
    public TagDto create(TagRequestDto createDto) {
        return mapper.mapToDto(
            service.create(createDto));
    }

    @Override
    public TagDto updateById(long id, Map<String, Object> newFieldValues) {
        return mapper.mapToDto(
            service.updateById(id, newFieldValues));
    }

    @Override
    public void deleteById(long id) {
        service.deleteById(id);
    }

    @Override
    public Page<TagDto> findAll(Pageable pageable) {
        return service.findAll(pageable)
            .map(mapper::mapToDto);
    }
}
