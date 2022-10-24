package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Tag create(TagPutDto createDto) {
        try {
            Tag newTag = tagMapper.mapPutDtoToEntity(createDto);
            return tagRepository.saveAndFlush(newTag);
        } catch (DataIntegrityViolationException e) {
            throw createTagIntegrityViolationException(createDto.getName());
        }
    }

    @Override
    @Transactional
    public Tag updateById(long id, TagPutDto putDto) {
        findById(id);
        try {
            Tag tagToPut = tagMapper.mapPutDtoToEntity(putDto);
            tagToPut.setId(id);
            return tagRepository.saveAndFlush(tagToPut);
        } catch (DataIntegrityViolationException e) {
            throw createTagIntegrityViolationException(putDto.getName());
        }
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
            .orElseGet(() -> tagRepository.save(Tag.builder()
                                                    .name(name)
                                                    .build()));
    }

    private IntegrityViolationException createTagIntegrityViolationException(String tagName) {
        return new IntegrityViolationException(TAG_ENTITY_NAME, Tag_.NAME, tagName,
                                               ErrorDescription.TAG_ALREADY_EXISTS);
    }
}
