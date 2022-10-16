package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTagMappedFromPutDto;
import static ru.clevertec.ecl.test.supply.TagTestDataSupplier.getTagPutDto;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.mapping.TagDtoMapper;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl service;

    @Mock
    private TagRepository repository;

    @Mock
    private TagDtoMapper mapper;

    @Test
    void givenPageable_whenFindAll_thenReturnCorrectTagPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        Page<Tag> givenResult = new PageImpl<>(
            Arrays.asList(getTag()));
        when(repository.findAll(any(Pageable.class)))
            .thenReturn(givenResult);

        Page<Tag> actual = service.findAll(givenPageable);

        assertEquals(givenResult, actual);
    }

    @Test
    void givenId_whenFindById_thenReturnCorrectTag() {
        long givenId = 1;
        stubFindByIdOfRepository();

        Tag actual = service.findById(givenId);

        Tag expected = getTag();
        assertEquals(expected, actual);
    }

    @Test
    void givenNotExistingId_whenFindById_thenThrowObjectNotFoundException() {
        long givenIncorrectId = 1;
        when(repository.findById(givenIncorrectId))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> service.findById(givenIncorrectId));
    }

    @Test
    void givenPutDto_whenCreate_thenSaveAndReturnCorrectTag() {
        TagPutDto givenPutDto = getTagPutDto();
        when(mapper.mapPutDtoToEntity(givenPutDto))
            .thenReturn(getTagMappedFromPutDto());
        when(repository.save(getTagMappedFromPutDto()))
            .thenReturn(getTag());

        Tag actual = service.create(givenPutDto);

        Tag expected = getTag();
        assertEquals(expected, actual);
    }

    @Test
    void givenIdAndPutDto_whenReplaceById_thenSaveAndReturnCorrectTag() {
        long givenId = 1;
        TagPutDto givenPutDto = getTagPutDto();
        stubFindByIdOfRepository();
        stubSaveOfRepository();
        when(mapper.mapPutDtoToEntity(givenPutDto))
            .thenReturn(getTagMappedFromPutDto());

        Tag actual = service.updateById(givenId, givenPutDto);

        Tag expected = getTag();
        assertEquals(expected, actual);
    }

    @Test
    void givenId_whenDeleteById_thenCallRepositoryDelete() {
        long givenId = 1;
        stubFindByIdOfRepository();

        service.deleteById(givenId);

        verify(repository)
            .delete(getTag());
    }

    @Test
    void givenExistingTagName_whenFindOrCreateByName_thenReturnCorrectTag() {
        String givenTagName = "default-tag";
        when(repository.findByName(givenTagName))
            .thenReturn(Optional.of(getTag()));

        Tag actual = service.findOrCreateByName(givenTagName);

        Tag expected = getTag();
        assertEquals(expected, actual);
    }

    @Test
    void givenNewTagName_whenFindOrCreateByName_thenSaveAndReturnCorrectTag() {
        String givenNewTagName = "new-tag";
        when(repository.findByName(givenNewTagName))
            .thenReturn(Optional.empty());
        when(repository.save(new Tag(0L, "new-tag")))
            .thenReturn(new Tag(1L, "new-tag"));

        Tag actual = service.findOrCreateByName(givenNewTagName);

        Tag expected = new Tag(1L, "new-tag");
        assertEquals(expected, actual);
    }

    private void stubFindByIdOfRepository() {
        when(repository.findById(1L))
            .thenReturn(Optional.of(
                getTag()));
    }

    private void stubSaveOfRepository() {
        when(repository.save(any()))
            .then(returnsFirstArg());
    }
}