package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.IntegrityViolationException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.Tag_;
import ru.clevertec.ecl.model.projection.TagOfUser;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.constant.ErrorDescription;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@RequiredArgsConstructor
@Slf4j
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
    public TagOfUser findMostValuableTag() {
        return tagRepository.findMostUsedTagOfMostValuableUser();
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name)
                   .orElseThrow(() -> new EntityNotFoundException(TAG_ENTITY_NAME, Tag_.NAME, name));
    }

    @Override
    @Transactional
    public Tag create(TagCreateDto createDto) {
        try {
            Tag newTag = tagMapper.mapToTag(createDto);
            return tagRepository.saveAndFlush(newTag);
        } catch (DataIntegrityViolationException e) {
            log.info("Data integrity exception: {}", e.getMessage());
            throw createTagIntegrityViolationException(e, createDto.getName());
        }
    }

    @Override
    @Transactional
    public Tag updateById(long id, TagPutDto putDto) {
        try {
            Tag existingTag = findById(id);
            tagMapper.updateTag(existingTag, putDto);
            return tagRepository.saveAndFlush(existingTag);
        } catch (DataIntegrityViolationException e) {
            log.info("Data integrity exception: {}", e.getMessage());
            throw createTagIntegrityViolationException(e, putDto.getName());
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Tag tagToDelete = findById(id);
        tagRepository.delete(tagToDelete);
    }

    private IntegrityViolationException createTagIntegrityViolationException(Exception e, String tagName) {
        return new IntegrityViolationException(e, TAG_ENTITY_NAME, Tag_.NAME, tagName,
                                               ErrorDescription.TAG_ALREADY_EXISTS);
    }
}
