package ru.clevertec.ecl.service.impl;

import static java.util.stream.Collectors.toList;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.GiftCertificate_;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.entityGraph.EntityGraphNames;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository repository;
    private final GiftCertificateDtoMapper mapper;
    private final TagService tagService;

    @Override
    public Page<GiftCertificate> findByNameAndDescription(String nameSample, String descriptionSample,
                                                          Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
            .withMatcher(GiftCertificate_.NAME, GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher(GiftCertificate_.DESCRIPTION, GenericPropertyMatchers.contains().ignoreCase());
        GiftCertificate certificateExample = createCertificateExample(nameSample, descriptionSample);
        return repository.findAll(Example.of(certificateExample, exampleMatcher),
                                  pageable,
                                  NamedEntityGraph.fetching(EntityGraphNames.CERTIFICATE_WITH_TAGS));
    }

    @Override
    public Page<GiftCertificate> findByTagName(String tagName, Pageable pageable) {
        return repository.findByTagName(tagName, pageable);
    }

    @Override
    public GiftCertificate findById(long id) {
        return repository.findById(id, NamedEntityGraph.fetching(EntityGraphNames.CERTIFICATE_WITH_TAGS))
            .orElseThrow(() -> new EntityNotFoundException("GiftCertificate", "id", id));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificateCreateDto creationDto) {
        GiftCertificate newCertificate = mapper.mapCreationDtoToEntity(creationDto);
        List<String> tagNames = creationDto.getTagNames();
        List<Tag> loadedTags = findOrCreateTags(tagNames);
        newCertificate.setTags(loadedTags);
        return repository.save(newCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto) {
        GiftCertificate giftCertificate = findByIdWithoutFetch(id);
        mapper.updateEntityIgnoringTags(giftCertificate, updateDto);
        updateTagsIfPresent(giftCertificate, updateDto.getTagNames());
        return repository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        GiftCertificate certificate = findByIdWithoutFetch(id);
        repository.delete(certificate);
    }

    private GiftCertificate findByIdWithoutFetch(long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("GiftCertificate", "id", id));
    }

    private List<Tag> findOrCreateTags(List<String> tagNames) {
        return tagNames.stream()
            .map(tagService::findOrCreateByName)
            .collect(toList());
    }

    private void updateTagsIfPresent(GiftCertificate certificate, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }
        List<Tag> loadedTags = findOrCreateTags(tagNames);
        certificate.setTags(loadedTags);
    }

    private GiftCertificate createCertificateExample(String name, String description) {
        return GiftCertificate.builder()
            .name(name)
            .description(description)
            .build();
    }
}
