package ru.clevertec.ecl.service.impl;

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.specification.GiftCertificateSpecifications;
import ru.clevertec.ecl.service.GiftCertificateService;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@RequiredArgsConstructor
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String CERTIFICATE_WITH_TAGS_ENTITY_GRAPH = "certificate-with-tags";

    private final GiftCertificateRepository repository;
    private final GiftCertificateDtoMapper mapper;
    private final TagService tagService;

    @Override
    public Page<GiftCertificate> findAllByFilters(Pageable pageable, GiftCertificateFilterDto filterDto) {
        Specification<GiftCertificate> isNameLikeSample = GiftCertificateSpecifications
            .isNameLikeOptional(filterDto.getNameSample());

        Specification<GiftCertificate> isDescriptionLikeSample = GiftCertificateSpecifications
            .isDescriptionLikeOptional(filterDto.getDescriptionSample());

        Specification<GiftCertificate> isTagPresent = GiftCertificateSpecifications
            .isTagPresentOptional(filterDto.getTagName());

        return repository.findAll(
            Specification.where(isNameLikeSample
                .and(isDescriptionLikeSample)
                .and(isTagPresent)),
            pageable,
            NamedEntityGraph.fetching(CERTIFICATE_WITH_TAGS_ENTITY_GRAPH));
    }

    @Override
    public GiftCertificate findById(long id) throws ObjectNotFoundException {
        return repository.findById(id, NamedEntityGraph.fetching(CERTIFICATE_WITH_TAGS_ENTITY_GRAPH))
            .orElseThrow(() -> new ObjectNotFoundException("GiftCertificate", "id", id));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificateCreateDto creationDto) {
        GiftCertificate newCertificate = mapper.mapCreationDtoToEntity(creationDto);
        List<String> tags = creationDto.getTagNames();
        List<Tag> loadedTags = findOrCreateTags(tags);
        newCertificate.setTags(loadedTags);
        return repository.save(newCertificate);
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, GiftCertificateUpdateDto updateDto) throws ObjectNotFoundException {
        GiftCertificate giftCertificate = findByIdOrThrow(id);
        mapper.updateEntityIgnoringTags(updateDto, giftCertificate);
        updateTagsIfPresent(giftCertificate, updateDto.getTagNames());
        return repository.save(giftCertificate);
    }

    @Override
    @Transactional
    public void deleteById(long id) throws ObjectNotFoundException {
        GiftCertificate certificate = findByIdOrThrow(id);
        repository.delete(certificate);
    }

    private GiftCertificate findByIdOrThrow(long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("GiftCertificate", "id", id));
    }

    private List<Tag> findOrCreateTags(List<String> tagNames) throws ObjectNotFoundException {
        return tagNames.stream()
            .map(tagService::findOrCreateByName)
            .collect(Collectors.toList());
    }

    private void updateTagsIfPresent(GiftCertificate certificate, List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }
        List<Tag> loadedTags = findOrCreateTags(tagNames);
        certificate.setTags(loadedTags);
    }
}
