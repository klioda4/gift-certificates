package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getListOfSingleTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTag;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagCreateDto;
import static ru.clevertec.ecl.test.supply.TagDataSupplier.getTagPutDto;

import java.util.Optional;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.IntegrityViolationException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.projection.TagOfUser;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.test.stub.StubHelper;
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
    void whenFindMostValuableTag_thenReturnExpectedTagOfUser() {
        when(tagRepository.findMostUsedTagOfMostValuableUser())
            .thenReturn(getTagOfUser());

        TagOfUser actualTagOfUser = tagService.findMostValuableTag();

        TagOfUser expectedTagOfUser = getTagOfUser();
        assertEquals(expectedTagOfUser, actualTagOfUser);
    }

    @Test
    void givenCreateDto_whenCreate_thenSaveAndReturnExpectedTag() {
        TagCreateDto givenCreateDto = getTagCreateDto();
        StubHelper.stubRepositorySaveAndFlush(tagRepository);
        stubMapperMapToTag();

        Tag actualTag = tagService.create(givenCreateDto);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenCreateDtoWithNotUniqueTagName_whenCreate_thenThrowIntegrityViolationException() {
        TagCreateDto givenCreateDto = getTagCreateDto();
        stubMapperMapToTag();
        when(tagRepository.saveAndFlush(getTag()))
            .thenThrow(DataIntegrityViolationException.class);

        assertThrows(IntegrityViolationException.class,
                     () -> tagService.create(givenCreateDto));
    }

    @Test
    void givenIdAndPutDto_whenUpdateById_thenSaveAndReturnExpectedTag() {
        long givenId = 1;
        TagPutDto givenPutDto = getTagPutDto();
        stubRepositoryFindById();
        StubHelper.stubRepositorySaveAndFlush(tagRepository);

        Tag actualTag = tagService.updateById(givenId, givenPutDto);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenPutDtoWithNotUniqueTagName_whenUpdateById_thenThrowIntegrityViolationException() {
        long givenId = 1;
        TagPutDto givenPutDto = getTagPutDto();
        stubRepositoryFindById();
        when(tagRepository.saveAndFlush(any()))
            .thenThrow(DataIntegrityViolationException.class);

        assertThrows(IntegrityViolationException.class,
                     () -> tagService.updateById(givenId, givenPutDto));
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

        Tag actualTag = tagService.findByName(givenTagName);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    @Test
    void givenName_whenFindByName_thenReturnExpectedTag() {
        String givenTagName = "default-tag";
        when(tagRepository.findByName(givenTagName))
            .thenReturn(Optional.of(getTag()));

        Tag actualTag = tagService.findByName(givenTagName);

        Tag expectedTag = getTag();
        assertEquals(expectedTag, actualTag);
    }

    private TagOfUser getTagOfUser() {
        return new TagOfUserImpl(1L, "someName", 5L);
    }

    private void stubRepositoryFindById() {
        when(tagRepository.findById(1L))
            .thenReturn(Optional.of(
                getTag()));
    }

    private void stubMapperMapToTag() {
        when(tagMapper.mapToTag(getTagCreateDto()))
            .thenReturn(getTag());
    }

    @Value
    private static class TagOfUserImpl implements TagOfUser {

        Long tagId;
        String tagName;
        Long userId;

        @Override
        public Long getTagId() {
            return tagId;
        }

        @Override
        public String getTagName() {
            return tagName;
        }

        @Override
        public Long getUserId() {
            return userId;
        }
    }
}
