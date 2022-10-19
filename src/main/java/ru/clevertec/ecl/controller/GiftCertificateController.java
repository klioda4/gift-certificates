package ru.clevertec.ecl.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificatePriceUpdateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.dto.response.GiftCertificateDto;
import ru.clevertec.ecl.facade.GiftCertificateFacade;
import ru.clevertec.ecl.util.validate.constraint.NotBlankOrNull;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/gift-certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateFacade certificateFacade;

    @GetMapping
    public Page<GiftCertificateDto> findAllByNameAndDescription(@NotBlankOrNull String nameSample,
                                                                @NotBlankOrNull String descriptionSample,
                                                                Pageable pageable) {
        log.info("GET request to /v1/gift-certificates with params: nameSample={}, descriptionSample={}, "
                     + "pageable={}", nameSample, descriptionSample, pageable);
        return certificateFacade.findAllByNameAndDescription(nameSample, descriptionSample, pageable);
    }

    @GetMapping("/filtered/by-tags")
    public Page<GiftCertificateDto> findAllByTagNames(@RequestParam("tag") @NotEmpty String[] tagNames,
                                                      Pageable pageable) {
        log.info("GET request to /v1/gift-certificates/filtered/by-tags with params: tagNames = {}, pageable = {}",
                 tagNames, pageable);
        return certificateFacade.findAllByTagNames(listOf(tagNames), pageable);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable @Positive long id) {
        return certificateFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateCreateDto createDto) {
        return certificateFacade.create(createDto);
    }

    @PutMapping("/{id}")
    public GiftCertificateDto updateById(@PathVariable @Positive long id,
                                         @RequestBody @Valid GiftCertificateUpdateDto updateDto) {
        return certificateFacade.updateById(id, updateDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePriceById(@PathVariable @Positive long id,
                                @RequestBody @Valid GiftCertificatePriceUpdateDto priceUpdateDto) {
        certificateFacade.updatePriceById(id, priceUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Positive long id) {
        certificateFacade.deleteById(id);
    }

    private <T> List<T> listOf(T[] array) {
        return Collections.unmodifiableList(
            Arrays.asList(array));
    }
}
