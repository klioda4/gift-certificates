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
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
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

    private static final String CERTIFICATE_ENTITY_NAME = "GiftCertificate";
    private static final int FIRST_INDEX = 0;

    private final GiftCertificateRepository certificateRepository;
    private final GiftCertificateDtoMapper certificateMapper;
    private final TagService tagService;

    @Override
    public Page<GiftCertificate> findAllByNameAndDescription(String nameSample, String descriptionSample,
                                                             Pageable pageable) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                                            .withMatcher(GiftCertificate_.NAME,
                                                         GenericPropertyMatchers.contains().ignoreCase())
                                            .withMatcher(GiftCertificate_.DESCRIPTION,
                                                         GenericPropertyMatchers.contains().ignoreCase());
        GiftCertificate certificateExample = createCertificateExample(nameSample, descriptionSample);
        return certificateRepository.findAll(Example.of(certificateExample, exampleMatcher),
                                             pageable,
                                             NamedEntityGraph.fetching(EntityGraphNames.CERTIFICATE_WITH_TAGS));
    }

    @Override
    public Page<GiftCertificate> findAllByTagNames(List<String> tagNames, Pageable pageable) {
        if (tagNames.size() == 1) {
            return certificateRepository.findAllByTagName(tagNames.get(FIRST_INDEX), pageable);
        } else {
            return certificateRepository.findAllByAllTagNames(tagNames, pageable);
        }
    }

    @Override
    public GiftCertificate findById(long id) {
        return certificateRepository.findById(id, NamedEntityGraph.fetching(EntityGraphNames.CERTIFICATE_WITH_TAGS))
                   .orElseThrow(() -> new EntityNotFoundException(CERTIFICATE_ENTITY_NAME, GiftCertificate_.ID, id));
    }

    @Override
    public GiftCertificate findByIdLazy(long id) {
        return certificateRepository.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException(CERTIFICATE_ENTITY_NAME, GiftCertificate_.ID, id));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificateCreateDto creationDto) {
        GiftCertificate newCertificate = certificateMapper.mapCreationDtoToEntity(creationDto);
        List<String> tagNames = creationDto.getTagNames();
        List<Tag> loadedTags = findOrCreateTags(tagNames);
        newCertificate.setTags(loadedTags);
        return certificateRepository.save(newCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto) {
        GiftCertificate giftCertificate = findById(id);
        certificateMapper.updateEntityIgnoringTags(giftCertificate, updateDto);
        updateTagsIfPresent(giftCertificate, updateDto.getTagNames());
        return certificateRepository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void updatePriceById(long id, GiftCertificatePriceUpdateDto priceUpdateDto) {
        GiftCertificate giftCertificate = findByIdLazy(id);
        giftCertificate.setPrice(priceUpdateDto.getPrice());
        certificateRepository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        GiftCertificate certificate = findByIdLazy(id);
        certificateRepository.delete(certificate);
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
