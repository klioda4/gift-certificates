package ru.clevertec.ecl.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.ecl.model.GiftCertificate;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@DataJpaTest
@ActiveProfiles("test")
class GiftCertificateRepositoryTest {

    private final GiftCertificateRepository certificateRepository;

    @Test
    void givenTagName_whenFindAllByTagName_thenReturnExpectedNumberOfCertificates() {
        String givenTagName = "sweetness";

        Page<GiftCertificate> resultCertificatePage = certificateRepository.findAllByTagName(givenTagName,
                                                                                             Pageable.ofSize(20));

        int expectedNumberOfCertificates = 3;
        assertEquals(expectedNumberOfCertificates, resultCertificatePage.getNumberOfElements());
    }

    @Test
    void givenTagNamesList_whenFindAllByAllTagNames_thenReturnExpectedNumberOfCertificates() {
        List<String> givenTagNames = Arrays.asList("sweetness", "holiday");

        Page<GiftCertificate> resultCertificatePage = certificateRepository.findAllByAllTagNames(givenTagNames,
                                                                                                 Pageable.ofSize(20));

        int expectedNumberOfCertificates = 2;
        assertEquals(expectedNumberOfCertificates, resultCertificatePage.getNumberOfElements());
    }
}
