package ru.clevertec.ecl.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.GiftCertificateCreationDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@RequiredArgsConstructor
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String TAGS_FIELD = "tags";

    private final GiftCertificateRepository repository;
    private final GiftCertificateDtoMapper mapper;
    private final TagService tagService;

    @Override
//    @Transactional(readOnly = true)
    public Page<GiftCertificate> findByFilters(Pageable pageable, GiftCertificateFilterDto filterDto) {
        ExampleMatcher filtersMatcher = ExampleMatcher.matchingAll()
            .withMatcher(NAME_FIELD, GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher(DESCRIPTION_FIELD, GenericPropertyMatchers.contains().ignoreCase());
        GiftCertificate certificateSample = GiftCertificate.builder()
            .name(filterDto.getNameSample())
            .description(filterDto.getDescriptionSample())
            .build();
        Example<GiftCertificate> filterExample = Example.of(certificateSample, filtersMatcher);
        return repository.findAll(filterExample, pageable);
    }

    @Override
//    @Transactional(readOnly = true)
    public GiftCertificate findById(long id) throws ObjectNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("GiftCertificate", "id", id));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificateCreationDto creationDto) {
        GiftCertificate newCertificate = mapper.mapCreationDtoToEntity(creationDto);
        setDatesForNewCertificate(newCertificate);
        List<String> tags = creationDto.getTags();
        List<Tag> loadedTags = findOrCreateAll(tags);
        newCertificate.setTags(loadedTags);
        return repository.save(newCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, Map<String, Object> newFieldValues) throws ObjectNotFoundException {
        GiftCertificate giftCertificate = findById(id);
        replaceTagNamesWithLoadedTagsIfExist(newFieldValues);
        populateFields(giftCertificate, newFieldValues);
        return repository.save(giftCertificate);
    }

    @Override
    public void deleteById(long id) throws ObjectNotFoundException {
        repository.deleteById(id);
    }

    private <T> void populateFields(T entity, Map<String, Object> newFieldValues) {
        try {
            BeanUtils.populate(entity, newFieldValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Incorrect field name or value was passed to update", e);
        }
    }

    private void setDatesForNewCertificate(GiftCertificate giftCertificate) {
        LocalDateTime currentTime = LocalDateTime.now();
        giftCertificate.setCreateDate(currentTime);
        giftCertificate.setLastUpdateDate(currentTime);
    }

    private List<Tag> findOrCreateAll(List<String> tagNames) throws ObjectNotFoundException {
        return tagNames.stream()
            .map(tagService::findOrCreateByName)
            .collect(Collectors.toList());
    }

    private void replaceTagNamesWithLoadedTagsIfExist(Map<String, Object> fieldValues) {
        // TODO: replace with better solution
        Object tagsNewValue = fieldValues.get(TAGS_FIELD);
        if (tagsNewValue == null) {
            return;
        }
        try {
            List<String> tags = (List<String>) tagsNewValue;
            List<Tag> loadedTags = findOrCreateAll(tags);
            fieldValues.put(TAGS_FIELD, loadedTags);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incorrect value for field \"tags\"", e);
        }
    }
}
