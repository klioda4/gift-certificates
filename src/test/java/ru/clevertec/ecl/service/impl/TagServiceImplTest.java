package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getListOfSingleTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagMappedFromPutDto;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagPutDto;

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
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagDtoMapper tagMapper;

    @Test
    void givenPageable_whenFindAll_thenReturnExpectedTagPage() {
        Pageable givenPageable = Pageable.ofSize(20);
        Page<Tag> expectedTagPage = new PageImpl<>(getListOfSingleTag());
        when(tagRepository.findAll(givenPageable))
            .thenReturn(expectedTagPage);

        Page<Tag> actualTagPage = tagService.findAll(givenPageable);

        assertEquals(expectedTagPage, actualTagPage);
    }

    @Test
    void givenId_whenFindById_thenReturnExpectedTag() {
        long givenId = 1;
        stubRepositoryFindById();

        Tag actualTag = tagService.findById(givenId);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenNotExistingId_whenFindById_thenThrowObjectNotFoundException() {
        long givenIncorrectId = 1;
        when(tagRepository.findById(givenIncorrectId))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                     () -> tagService.findById(givenIncorrectId));
    }

    @Test
    void givenPutDto_whenCreate_thenSaveAndReturnExpectedTag() {
        TagPutDto givenPutDto = getTagPutDto();
        when(tagMapper.mapPutDtoToEntity(givenPutDto))
            .thenReturn(getTagMappedFromPutDto());
        when(tagRepository.save(getTagMappedFromPutDto()))
            .thenReturn(getTag());

        Tag actualTag = tagService.create(givenPutDto);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenIdAndPutDto_whenReplaceById_thenSaveAndReturnExpectedTag() {
        long givenId = 1;
        TagPutDto givenPutDto = getTagPutDto();
        stubRepositoryFindById();
        stubRepositorySave();
        when(tagMapper.mapPutDtoToEntity(givenPutDto))
            .thenReturn(getTagMappedFromPutDto());

        Tag actualTag = tagService.updateById(givenId, givenPutDto);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenId_whenDeleteById_thenCallRepositoryDelete() {
        long givenId = 1;
        stubRepositoryFindById();

        tagService.deleteById(givenId);

        verify(tagRepository)
            .delete(getTag());
    }

    @Test
    void givenExistingTagName_whenFindOrCreateByName_thenReturnExpectedTag() {
        String givenTagName = "default-tag";
        when(tagRepository.findByName(givenTagName))
            .thenReturn(Optional.of(getTag()));

        Tag actualTag = tagService.findOrCreateByName(givenTagName);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenNewTagName_whenFindOrCreateByName_thenSaveAndReturnExpectedTag() {
        String givenNewTagName = "new-tag";
        when(tagRepository.findByName(givenNewTagName))
            .thenReturn(Optional.empty());
        when(tagRepository.save(new Tag(0L, "new-tag")))
            .thenReturn(new Tag(1L, "new-tag"));

        Tag actualTag = tagService.findOrCreateByName(givenNewTagName);

        Tag expectedTag = new Tag(1L, "new-tag");
        assertEquals(expectedTag, actualTag);
    }

    private void stubRepositoryFindById() {
        when(tagRepository.findById(1L))
            .thenReturn(Optional.of(
                getTag()));
    }

    private void stubRepositorySave() {
        when(tagRepository.save(any()))
            .then(returnsFirstArg());
    }
}
