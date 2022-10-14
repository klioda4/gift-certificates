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
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificate;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateMappedFromCreateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateMergedWithUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getGiftCertificateUpdateDto;
import static ru.clevertec.ecl.test.supply.GiftCertificateDataSupplier.getListOfSingleGiftCertificate;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getListOfSingleTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTag;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph;
import java.util.Collections;
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
    private GiftCertificateServiceImpl certificateService;

    @Mock
    private GiftCertificateRepository certificateRepository;

    @Mock
    private GiftCertificateDtoMapper certificateMapper;

    @Mock
    private TagService tagService;

    @Test
    void givenPageableAndAnyNameSample_whenFindByNameAndDescription_thenReturnExpectedPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> stubbedResult = new PageImpl<>(getListOfSingleGiftCertificate());
        when(
            certificateRepository.findAll(
                any(Example.class),
                any(Pageable.class),
                any(NamedEntityGraph.class)))
            .thenReturn(stubbedResult);

        Page<GiftCertificate> actualCertificate = certificateService.findAllByNameAndDescription("any", null,
                                                                                                 givenPageable);

        assertEquals(stubbedResult, actualCertificate);
    }

    @Test
    void givenPageableAndTagName_whenFindByTagName_thenReturnCorrectPage() {
        String givenTagName = "test";
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> stubbedResult = new PageImpl<>(getListOfSingleGiftCertificate());
        when(certificateRepository.findByTagName(givenTagName, givenPageable))
            .thenReturn(stubbedResult);

        Page<GiftCertificate> actual = certificateService.findByTagName(givenTagName, givenPageable);

        assertEquals(stubbedResult, actual);
    }

    @Test
    void givenId_whenFindById_thenReturnExpectedGiftCertificate() {
        long givenId = 1;
        when(
            certificateRepository.findById(
                eq(givenId),
                any(EntityGraph.class)))
            .thenReturn(Optional.of(
                getGiftCertificate()));

        GiftCertificate actualCertificate = certificateService.findById(givenId);

        GiftCertificate expectedCertificate = getGiftCertificate();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenNotExistingId_whenFindById_thenThrowObjectNotFoundException() {
        long givenIncorrectId = 1;
        when(
            certificateRepository.findById(
                eq(givenIncorrectId),
                any(EntityGraph.class)))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                     () -> certificateService.findById(givenIncorrectId));
    }

    @Test
    void givenCreateDtoWithoutTags_whenCreate_thenSaveAndReturnExpectedGiftCertificate() {
        GiftCertificateCreateDto givenCreateDto = getGiftCertificateCreateDto();
        when(certificateMapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(getGiftCertificateMappedFromCreateDto());
        when(certificateRepository.save(getGiftCertificateMappedFromCreateDto()))
            .thenReturn(getGiftCertificate());

        GiftCertificate actualCertificate = certificateService.create(givenCreateDto);

        GiftCertificate expectedCertificate = getGiftCertificate();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenCreateDtoWithTags_whenCreate_thenReturnGiftCertificateWithExpectedTags() {
        GiftCertificateCreateDto givenCreateDto = GiftCertificateCreateDto.builder()
            .tagNames(Collections.singletonList("default-tag"))
            .build();
        stubRepositorySave();
        when(certificateMapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(new GiftCertificate());
        when(tagService.findOrCreateByName("default-tag"))
            .thenReturn(getTag());

        GiftCertificate certificate = certificateService.create(givenCreateDto);
        List<Tag> actualTags = certificate.getTags();

        List<Tag> expectedTags = getListOfSingleTag();
        assertNotNull(actualTags, "tags field needs to not be null");
        assertEquals(expectedTags, actualTags, "tags field was not mapped correctly");
    }

    @Test
    void givenIdAndUpdateDto_whenUpdateById_thenUpdateOnlySpecifiedFields() {
        long givenId = 1;
        GiftCertificateUpdateDto givenUpdateDto = getGiftCertificateUpdateDto();
        stubRepositoryFindById(givenId);
        stubRepositorySave();
        stubMapperPartialUpdate();

        GiftCertificate actualCertificate = certificateService.updateById(givenId, givenUpdateDto);

        GiftCertificate expectedCertificate = getGiftCertificateMergedWithUpdateDto();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenIdAndUpdateDtoWithTagNames_whenUpdateById_thenUpdateTagsInGiftCertificate() {
        long givenId = 1;
        GiftCertificateUpdateDto givenUpdateDto = GiftCertificateUpdateDto.builder()
            .tagNames(Collections.singletonList("default-tag"))
            .build();
        when(certificateRepository.findById(givenId))
            .thenReturn(Optional.of(getGiftCertificate()));
        when(certificateRepository.save(any()))
            .then(returnsFirstArg());
        when(tagService.findOrCreateByName("default-tag"))
            .thenReturn(getTag());

        GiftCertificate resultCertificate = certificateService.updateById(givenId, givenUpdateDto);

        List<Tag> expectedTags = getListOfSingleTag();
        assertEquals(expectedTags, resultCertificate.getTags(), "Tags field was updated incorrectly");
    }

    @Test
    void givenId_whenDeleteById_thenCallRepositoryDelete() {
        long givenId = 1L;
        stubRepositoryFindById(givenId);

        certificateService.deleteById(givenId);

        verify(certificateRepository)
            .delete(getGiftCertificate());
    }

    private void stubRepositoryFindById(long id) {
        when(certificateRepository.findById(id))
            .thenReturn(Optional.of(
                getGiftCertificate()));
    }

    private void stubRepositorySave() {
        when(certificateRepository.save(any()))
            .then(returnsFirstArg());
    }

    private void stubMapperPartialUpdate() {
        doAnswer(invocation -> {
            GiftCertificate certificateForUpdate = invocation.getArgument(0);
            certificateForUpdate.setName("new_name");
            certificateForUpdate.setDuration(55);
            return null;
        })
            .when(certificateMapper).updateEntityIgnoringTags(
                any(GiftCertificate.class),
                eq(getGiftCertificateUpdateDto()));
    }
}
