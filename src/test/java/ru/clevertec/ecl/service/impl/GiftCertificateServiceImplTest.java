package ru.clevertec.ecl.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.util.mapping.ext.GiftCertificateMapper;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private static GiftCertificateServiceImpl testService;
    private static JpaRepository<GiftCertificate, Long> mockRepository;

    @BeforeAll
    static void beforeAll() {
        GiftCertificateMapper mapper = Mappers.getMapper(GiftCertificateMapper.class);
        mockRepository = Mockito.mock(JpaRepository.class);
        testService = new GiftCertificateServiceImpl(mockRepository, mapper);
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

        GiftCertificateDto actualDto = testService.updateById(givenId, givenUpdateValues);
        GiftCertificateDto expectedDto = getExpectedDtoAfterUpdate();
        assertEquals(expectedDto, actualDto);
    }

    private GiftCertificate createGiftCertificate() {
        return GiftCertificate.builder()
            .id(1L)
            .name("cert")
            .description("description")
            .price(BigDecimal.valueOf(5L))
            .createDate(LocalDate.of(2050, 1, 1))
            .lastUpdateDate(LocalDate.of(2050, 1, 2))
            .tags(new HashSet<>())
            .build();
    }

    private GiftCertificateDto getExpectedDtoAfterUpdate() {
        return GiftCertificateDto.builder()
            .id(1L)
            .name("new_name")
            .description("new_description")
            .price(BigDecimal.valueOf(5L))
            .createDate(LocalDate.of(2050, 1, 1))
            .lastUpdateDate(LocalDate.of(2050, 1, 2))
            .tags(new HashSet<>())
            .build();
    }
}