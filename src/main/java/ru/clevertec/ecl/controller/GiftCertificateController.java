package ru.clevertec.ecl.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.facade.GiftCertificateFacade;
import ru.clevertec.ecl.util.validate.constraint.NotBlankOrNull;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/gift-certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateFacade facade;

    @GetMapping
    public Page<GiftCertificateDto> findByNameAndDescription(@NotBlankOrNull String nameSample,
                                                             @NotBlankOrNull String descriptionSample,
                                                             Pageable pageable) {
        log.info("GET request to /api/v1/gift-certificates with params: nameSample={}, descriptionSample={}, "
                     + "pageable={}", nameSample, descriptionSample, pageable);
        return facade.findByNameAndDescription(nameSample, descriptionSample, pageable);
    }

    @GetMapping("/tag/{tagName}")
    public Page<GiftCertificateDto> findByTagName(@PathVariable @NotBlank String tagName,
                                                  Pageable pageable) {
        log.info("GET request to /api/v1/gift-certificates/tag/{tagName} with params: tagName = {}, pageable = {}",
                 tagName, pageable);
        return facade.findByTagName(tagName, pageable);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable long id) {
        return facade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateCreateDto newItem) {
        return facade.create(newItem);
    }

    @PutMapping("/{id}")
    public GiftCertificateDto updateById(@PathVariable long id,
                                         @RequestBody @Valid GiftCertificateUpdateDto updateDto) {
        return facade.updateById(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        facade.deleteById(id);
    }
}
