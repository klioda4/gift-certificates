package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificate;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMappedFromCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateMergedWithUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateTestDataSupplier.getGiftCertificateUpdateDto;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @InjectMocks
    private GiftCertificateServiceImpl service;

    @Mock
    private GiftCertificateRepository repository;

    @Mock
    private TagService tagService;

    @Mock
    private GiftCertificateDtoMapper mapper;

    @Test
    void givenPageable_whenFindAllByFilters_thenReturnCorrectGiftCertificatePage() {
        Pageable givenPageable = Pageable.ofSize(20);
        GiftCertificateFilterDto givenFilterDto = new GiftCertificateFilterDto();
        Page<GiftCertificate> givenResult = new PageImpl<>(
            Collections.singletonList(getGiftCertificate()));
        when(
            repository.findAll(
                any(Specification.class),
                any(Pageable.class),
                any(NamedEntityGraph.class)))
            .thenReturn(givenResult);

        Page<GiftCertificate> actual = service.findAllByFilters(givenPageable, givenFilterDto);

        assertEquals(givenResult, actual);
    }

    @Test
    void givenId_whenFindById_thenReturnCorrectGiftCertificate() {
        long givenId = 1L;
        when(
            repository.findById(
                eq(givenId),
                any(EntityGraph.class)))
            .thenReturn(Optional.of(
                getGiftCertificate()));

        GiftCertificate actual = service.findById(givenId);

        GiftCertificate expected = getGiftCertificate();
        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingId_whenFindById_thenThrowObjectNotFoundException() {
        long givenIncorrectId = 1L;
        when(
            repository.findById(
                eq(givenIncorrectId),
                any(EntityGraph.class)))
            .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
            () -> service.findById(givenIncorrectId));
    }

    @Test
    void givenCreateDtoWithoutTags_whenCreate_thenSaveAndReturnCorrectGiftCertificate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();
        when(mapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(getGiftCertificateMappedFromCreateDto());
        when(repository.save(getGiftCertificateMappedFromCreateDto()))
            .thenReturn(getGiftCertificate());

        GiftCertificate actual = service.create(givenCreateDto);

        GiftCertificate expected = getGiftCertificate();
        assertEquals(expected, actual);
    }

    @Test
    void givenCreateDtoWithTags_whenCreate_thenReturnGiftCertificateWithCorrectTags() {
        GiftCertificateCreateDto givenCreateDto = GiftCertificateCreateDto.builder()
            .tagNames(Arrays.asList("tag1"))
            .build();
        stubSaveOfRepository();
        when(mapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(new GiftCertificate());
        when(tagService.findOrCreateByName("tag1"))
            .thenReturn(new Tag(1L, "tag1"));

        GiftCertificate actual = service.create(givenCreateDto);

        List<Tag> expectedList = Arrays.asList(new Tag(1L, "tag1"));
        assertNotNull(actual.getTags(), "tags field needs to not be null");
        assertEquals(expectedList, actual.getTags(), "tags field was not mapped correctly");
    }

    @Test
    void givenIdAndUpdateDto_whenUpdateById_thenUpdateOnlySpecifiedFields() {
        long givenId = 1;
        GiftCertificateUpdateDto givenUpdateDto = GiftCertificateUpdateDto.builder()
            .name("new_name")
            .duration(55)
            .build();
        stubFindByIdOfRepository(givenId);
        stubSaveOfRepository();
        stubPartialUpdateOfMapper();

        GiftCertificate actual = service.updateById(givenId, givenUpdateDto);

        GiftCertificate expected = getGiftCertificateMergedWithUpdateDto();
        assertEquals(expected, actual);
    }

    @Test
    void givenIdAndUpdateDtoWithTagNames_whenUpdateById_thenUpdateTagsInGiftCertificate() {
        long givenId = 1;
        GiftCertificateUpdateDto givenUpdateDto = GiftCertificateUpdateDto.builder()
            .tagNames(Arrays.asList("tag1", "tag2"))
            .build();
        when(repository.findById(givenId))
            .thenReturn(Optional.of(getGiftCertificate()));
        when(repository.save(any()))
            .then(returnsFirstArg());
        when(tagService.findOrCreateByName("tag1"))
            .thenReturn(new Tag(1L, "tag1"));
        when(tagService.findOrCreateByName("tag2"))
            .thenReturn(new Tag(2L, "tag2"));

        GiftCertificate resultCertificate = service.updateById(givenId, givenUpdateDto);

        List<Tag> expectedTags = Arrays.asList(
            new Tag(1L, "tag1"),
            new Tag(2L, "tag2"));
        assertEquals(expectedTags, resultCertificate.getTags(), "Tags field was updated incorrectly");
    }

    @Test
    void givenId_whenDeleteById_thenCallRepositoryDelete() {
        long givenId = 1L;
        stubFindByIdOfRepository(givenId);

        service.deleteById(givenId);

        verify(repository)
            .delete(getGiftCertificate());
    }

    private void stubFindByIdOfRepository(long id) {
        when(repository.findById(id))
            .thenReturn(Optional.of(
                getGiftCertificate()));
    }

    private void stubSaveOfRepository() {
        when(repository.save(any()))
            .then(returnsFirstArg());
    }

    private void stubPartialUpdateOfMapper() {
        doAnswer(invocation -> {
            GiftCertificate certificateForUpdate = invocation.getArgument(1);
            certificateForUpdate.setName("new_name");
            certificateForUpdate.setDuration(55);
            return null;
        })
            .when(mapper).updateEntityIgnoringTags(
                eq(getGiftCertificateUpdateDto()),
                any(GiftCertificate.class));
    }
}