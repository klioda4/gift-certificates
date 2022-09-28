package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.service.TagService;
import ru.clevertec.ecl.util.mapping.GiftCertificateDtoMapper;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private static GiftCertificateServiceImpl testService;
    private static GiftCertificateRepository mockRepository;
    private static TagService mockTagService;

    @BeforeAll
    static void beforeAll() {
        GiftCertificateDtoMapper mapper = Mappers.getMapper(GiftCertificateDtoMapper.class);
        mockTagService = Mockito.mock(TagService.class);
        mockRepository = Mockito.mock(GiftCertificateRepository.class);
        testService = new GiftCertificateServiceImpl(mockRepository, mapper, mockTagService);
    }

    @Test
    void givenIdAndMapOrFieldsWithNewValues_whenUpdateById_thenUpdateOnlySpecifiedFieldsAndReturnCorrectDto() {
        long givenId = 1;
        Map<String, Object> givenUpdateValues = new HashMap<>();
        givenUpdateValues.put("name", "new_name");
        givenUpdateValues.put("description", "new_description");
        when(mockRepository.findById(givenId))
            .thenReturn(Optional.of(createGiftCertificate()));
        when(mockRepository.save(any()))
            .then(returnsFirstArg());

        GiftCertificate actual = testService.updateById(givenId, givenUpdateValues);
        GiftCertificate expected = getExpectedDtoAfterUpdate();
        assertEquals(expected, actual);
    }

    private GiftCertificate createGiftCertificate() {
        return GiftCertificate.builder()
            .id(1L)
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 2, 0, 0))
            .tags(new ArrayList<>())
            .build();
    }

    private GiftCertificate getExpectedDtoAfterUpdate() {
        return GiftCertificate.builder()
            .id(1L)
            .name("new_name")
            .description("new_description")
            .price(BigDecimal.valueOf(5L))
            .createDate(LocalDateTime.of(2050, 1, 1, 0, 0))
            .lastUpdateDate(LocalDateTime.of(2050, 1, 2, 0, 0))
            .tags(new ArrayList<>())
            .build();
    }
}