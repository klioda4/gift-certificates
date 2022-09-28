package ru.clevertec.ecl.facade;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreationDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;

public interface GiftCertificateFacade {

    Page<GiftCertificateDto> findByFilter(Pageable pageable, GiftCertificateFilterDto filterDto);

    GiftCertificateDto findById(long id);

    GiftCertificateDto create(GiftCertificateCreationDto createDto);

    GiftCertificateDto updateById(long id, Map<String, Object> newFieldValues);

    void deleteById(long id);
}
