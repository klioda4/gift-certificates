package ru.clevertec.ecl.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.TagRequestDto;
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
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Tag", "id", id));
    }

    @Override
    public Tag create(TagRequestDto createDto) {
        Tag newTag = mapper.mapCreationDtoToEntity(createDto);
        return repository.save(newTag);
    }

    @Override
    @Transactional
    public Tag updateById(long id, Map<String, Object> newFieldValues) {
        Tag tagToUpdate = findById(id);
        populateFields(tagToUpdate, newFieldValues);
        return repository.save(tagToUpdate);
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

    private <T> void populateFields(T entity, Map<String, Object> newFieldValues) {
        try {
            BeanUtils.populate(entity, newFieldValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Incorrect field name or value was passed to update", e);
        }
    }

    private Tag saveByName(String name) {
        return repository.save(Tag.builder()
            .name(name)
            .build());
    }
}
