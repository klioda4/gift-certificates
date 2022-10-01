package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository repository;
    private final TagDtoMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<Tag> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findById(long id) throws ObjectNotFoundException {
        return findByIdOrThrow(id);
    }

    @Override
    public Tag create(TagPutDto createDto) {
        Tag newTag = mapper.mapPutDtoToEntity(createDto);
        return repository.save(newTag);
    }

    @Override
    @Transactional
    public Tag replaceById(long id, TagPutDto putDto) throws ObjectNotFoundException {
        findByIdOrThrow(id);
        Tag tagToPut = mapper.mapPutDtoToEntity(putDto);
        tagToPut.setId(id);
        return repository.save(tagToPut);
    }

    @Override
    @Transactional
    public void deleteById(long id) throws ObjectNotFoundException {
        Tag tagToDelete = findById(id);
        repository.delete(tagToDelete);
    }

    @Override
    @Transactional
    public Tag findOrCreateByName(String name) {
        return repository.findByName(name)
            .orElseGet(() -> saveByName(name));
    }

    private Tag saveByName(String name) {
        return repository.save(Tag.builder()
            .name(name)
            .build());
    }

    private Tag findByIdOrThrow(long id) throws ObjectNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Tag", "id", id));
    }
}
