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

    private static final String TAG_ENTITY_NAME = "Tag";

    private final TagRepository tagRepository;
    private final TagDtoMapper tagMapper;

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag findById(long id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(TAG_ENTITY_NAME, Tag_.ID, id));
    }

    @Override
    @Transactional
    public Tag create(TagPutDto createDto) {
        checkNameNotExists(createDto.getName());
        Tag newTag = tagMapper.mapPutDtoToEntity(createDto);
        return tagRepository.save(newTag);
    }

    @Override
    @Transactional
    public Tag updateById(long id, TagPutDto putDto) {
        findById(id);
        checkNameNotExists(putDto.getName());
        Tag tagToPut = tagMapper.mapPutDtoToEntity(putDto);
        tagToPut.setId(id);
        return tagRepository.save(tagToPut);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Tag tagToDelete = findById(id);
        tagRepository.delete(tagToDelete);
    }

    @Override
    @Transactional
    public Tag findOrCreateByName(String name) {
        return tagRepository.findByName(name)
            .orElseGet(() -> saveByName(name));
    }

    private Tag saveByName(String name) {
        return tagRepository.save(Tag.builder()
                                      .name(name)
                                      .build());
    }

    private void checkNameNotExists(String name) {
        Example<Tag> nameExample = Example.of(
            Tag.builder()
                .name(name)
                .build());
        if (tagRepository.exists(nameExample)) {
            throw new IntegrityViolationException(Tag.class.getSimpleName(), Tag_.NAME, name,
                                                  ErrorDescription.TAG_ALREADY_EXISTS);
        }
    }
}
