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
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTag;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
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
    void givenPageableAndAnyNameSample_whenFindByNameAndDescription_thenReturnCorrectPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> stubbedResult = new PageImpl<>(
            Arrays.asList(getGiftCertificate()));
        when(
            repository.findAll(
                any(Example.class),
                any(Pageable.class),
                any(NamedEntityGraph.class)))
            .thenReturn(stubbedResult);

        Page<GiftCertificate> actual = service.findByNameAndDescription("any", null, givenPageable);

        assertEquals(stubbedResult, actual);
    }

    @Test
    void givenPageableAndTagName_whenFindByTagName_thenReturnCorrectPage() {
        String givenTagName = "test";
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> stubbedResult = new PageImpl<>(
            Arrays.asList(getGiftCertificate()));
        when(repository.findByTagName(givenTagName, givenPageable))
            .thenReturn(stubbedResult);

        Page<GiftCertificate> actual = service.findByTagName(givenTagName, givenPageable);

        assertEquals(stubbedResult, actual);
    }

    @Test
    void givenId_whenFindById_thenReturnCorrectGiftCertificate() {
        long givenId = 1;
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
        long givenIncorrectId = 1;
        when(
            repository.findById(
                eq(givenIncorrectId),
                any(EntityGraph.class)))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
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
            .tagNames(Arrays.asList("default-tag"))
            .build();
        stubSaveOfRepository();
        when(mapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(new GiftCertificate());
        when(tagService.findOrCreateByName("default-tag"))
            .thenReturn(getTag());

        GiftCertificate actual = service.create(givenCreateDto);

        List<Tag> expectedList = Arrays.asList(getTag());
        assertNotNull(actual.getTags(), "tags field needs to not be null");
        assertEquals(expectedList, actual.getTags(), "tags field was not mapped correctly");
    }

    @Test
    void givenIdAndUpdateDto_whenUpdateById_thenUpdateOnlySpecifiedFields() {
        long givenId = 1;
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();
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
            .tagNames(Arrays.asList("default-tag"))
            .build();
        when(repository.findById(givenId))
            .thenReturn(Optional.of(getGiftCertificate()));
        when(repository.save(any()))
            .then(returnsFirstArg());
        when(tagService.findOrCreateByName("default-tag"))
            .thenReturn(getTag());

        GiftCertificate resultCertificate = service.updateById(givenId, givenUpdateDto);

        List<Tag> expectedTags = Arrays.asList(getTag());
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
            GiftCertificate certificateForUpdate = invocation.getArgument(0);
            certificateForUpdate.setName("new_name");
            certificateForUpdate.setDuration(55);
            return null;
        })
            .when(mapper).updateEntityIgnoringTags(
                any(GiftCertificate.class),
                eq(getGiftCertificateUpdateDto()));
    }
}