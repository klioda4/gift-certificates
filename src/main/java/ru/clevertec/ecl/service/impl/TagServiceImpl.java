package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.IntegrityViolationException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.Tag_;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.constant.ErrorDescription;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository repository;
    private final TagDtoMapper mapper;

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Tag findById(long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tag", "id", id));
    }

    @Override
    @Transactional
    public Tag create(TagPutDto createDto) {
        checkNameNotExists(createDto.getName());
        Tag newTag = mapper.mapPutDtoToEntity(createDto);
        return repository.save(newTag);
    }

    @Override
    @Transactional
    public Tag updateById(long id, TagPutDto putDto) {
        findById(id);
        checkNameNotExists(putDto.getName());
        Tag tagToPut = mapper.mapPutDtoToEntity(putDto);
        tagToPut.setId(id);
        return repository.save(tagToPut);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
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

    private void checkNameNotExists(String name) {
        Example<Tag> nameExample = Example.of(
            Tag.builder()
                .name(name)
                .build());
        if (repository.exists(nameExample)) {
            throw new IntegrityViolationException(Tag.class.getSimpleName(), Tag_.NAME, name,
                ErrorDescription.TAG_ALREADY_EXISTS);
        }
    }
}
