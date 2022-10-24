package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.test.stub.StubHelper;
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
    void givenPageableAndNameSample_whenFindAllByNameAndDescription_thenReturnExpectedPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        String givenNameSample = "any";
        Page<GiftCertificate> expectedCertificatePage = new PageImpl<>(getListOfSingleGiftCertificate());
        when(
            certificateRepository.findAll(
                any(Example.class),
                eq(givenPageable),
                any(NamedEntityGraph.class)))
            .thenReturn(expectedCertificatePage);

        Page<GiftCertificate> actualCertificate = certificateService.findAllByNameAndDescription(givenNameSample, null,
                                                                                                 givenPageable);

        assertEquals(expectedCertificatePage, actualCertificate);
    }

    @Test
    void givenListOfTwoTagsAndPageable_whenFindAllByTagNames_thenReturnExpectedPage() {
        List<String> givenTags = Arrays.asList("tag1", "tag2");
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> expectedCertificatePage = new PageImpl<>(getListOfSingleGiftCertificate());
        when(certificateRepository.findAllByAllTagNames(givenTags, givenPageable))
            .thenReturn(expectedCertificatePage);

        Page<GiftCertificate> actualCertificatePage = certificateService.findAllByTagNames(givenTags, givenPageable);

        assertEquals(expectedCertificatePage, actualCertificatePage);
    }

    @Test
    void givenOneTagAndPageable_whenFindAllByTagNames_thenReturnExpectedPage() {
        String givenTag = "tag1";
        Pageable givenPageable = Pageable.ofSize(20);
        Page<GiftCertificate> expectedCertificatePage = new PageImpl<>(getListOfSingleGiftCertificate());
        when(certificateRepository.findAllByTagName(givenTag, givenPageable))
            .thenReturn(expectedCertificatePage);

        Page<GiftCertificate> actualCertificatePage = certificateService.findAllByTagNames(
            Collections.singletonList(givenTag),
            givenPageable);
        assertEquals(expectedCertificatePage, actualCertificatePage);
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
    void givenNotExistingId_whenFindById_thenThrowEntityNotFoundException() {
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
    void givenId_whenFindByIdLazy_thenReturnExpectedCertificate() {
        long givenId = 1;
        stubRepositoryFindById(givenId);

        GiftCertificate actualCertificate = certificateService.findByIdLazy(givenId);

        GiftCertificate expectedCertificate = getGiftCertificate();
        assertEquals(expectedCertificate, actualCertificate);
    }

    @Test
    void givenNotExistingId_whenFindByIdLazy_thenThrowEntityNotFoundException() {
        long givenIncorrectId = 1;
        when(certificateRepository.findById(givenIncorrectId))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                     () -> certificateService.findByIdLazy(givenIncorrectId));
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
        StubHelper.stubRepositorySave(certificateRepository);
        stubTagServiceFindOrCreateByName();
        when(certificateMapper.mapCreationDtoToEntity(givenCreateDto))
            .thenReturn(new GiftCertificate());

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
        stubRepositoryFindByIdWithEntityGraph(givenId);
        stubMapperPartialUpdate();
        StubHelper.stubRepositorySave(certificateRepository);

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
        stubRepositoryFindByIdWithEntityGraph(givenId);
        stubTagServiceFindOrCreateByName();
        StubHelper.stubRepositorySave(certificateRepository);

        GiftCertificate resultCertificate = certificateService.updateById(givenId, givenUpdateDto);

        List<Tag> expectedTags = getListOfSingleTag();
        assertEquals(expectedTags, resultCertificate.getTags(), "Tags field was updated incorrectly");
    }

    @Test
    void givenIdAndPriceUpdateDto_whenUpdatePriceById_thenUpdatePriceAndCallSaveWithExpectedCertificate() {
        long givenId = 1;
        GiftCertificatePriceUpdateDto givenPriceUpdateDto = new GiftCertificatePriceUpdateDto(BigDecimal.TEN);
        GiftCertificate initialCertificate = GiftCertificate.builder().id(givenId).build();
        GiftCertificate updatedCertificate = GiftCertificate.builder()
            .id(givenId)
            .price(BigDecimal.TEN)
            .build();
        when(certificateRepository.findById(givenId))
            .thenReturn(Optional.of(initialCertificate));
        when(certificateRepository.save(updatedCertificate))
            .then(Answers.RETURNS_DEFAULTS);

        certificateService.updatePriceById(givenId, givenPriceUpdateDto);
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

    private void stubRepositoryFindByIdWithEntityGraph(long id) {
        when(certificateRepository.findById(eq(id),
                                            any(EntityGraph.class)))
            .thenReturn(Optional.of(
                getGiftCertificate()));
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

    private void stubTagServiceFindOrCreateByName() {
        when(tagService.findOrCreateByName("default-tag"))
            .thenReturn(getTag());
    }
}
