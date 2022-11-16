package ru.clevertec.ecl.controller;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<GiftCertificateDto>> findAllByNameAndDescription(@NotBlankOrNull String nameSample,
                                                                                @NotBlankOrNull String descriptionSample,
                                                                                Pageable pageable) {
        log.info("GET request to /v1/gift-certificates with params: nameSample={}, descriptionSample={}, pageable={}",
                 nameSample, descriptionSample, pageable);
        Page<GiftCertificateDto> certificatesPage = certificateFacade.findAllByNameAndDescription(nameSample,
                                                                                                  descriptionSample,
                                                                                                  pageable);
        log.info("Result = {}", certificatesPage);
        return ResponseEntity.ok(certificatesPage);
    }

    @GetMapping("/tags")
    public ResponseEntity<Page<GiftCertificateDto>> findAllByTagNames(
        @RequestParam("tag") @NotEmpty List<@NotBlank String> tagNames,
        Pageable pageable) {

        log.info("GET request to /v1/gift-certificates/filtered/by-tags with params: tagNames = {}, pageable = {}",
                 tagNames, pageable);
        Page<GiftCertificateDto> certificatesPage = certificateFacade.findAllByTagNames(tagNames, pageable);
        log.info("Result = {}", certificatesPage);
        return ResponseEntity.ok(certificatesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> findById(@PathVariable @Positive long id) {
        log.info("GET request to /v1/gift-certificates/{id} with id = {}", id);
        GiftCertificateDto certificateDto = certificateFacade.findById(id);
        log.info("Result = {}", certificateDto);
        return ResponseEntity.ok(certificateDto);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@RequestBody @Valid GiftCertificateCreateDto createDto) {
        log.info("POST request to /v1/gift-certificates with createDto = {}", createDto);
        GiftCertificateDto certificateDto = certificateFacade.create(createDto);
        log.info("Result = {}", certificateDto);
        return new ResponseEntity<>(certificateDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> updateById(@PathVariable @Positive long id,
                                                         @RequestBody @Valid GiftCertificateUpdateDto updateDto) {
        log.info("PUT request to /v1/gift-certificates/{id} with id = {}, updateDto = {}", id, updateDto);
        GiftCertificateDto certificateDto = certificateFacade.updateById(id, updateDto);
        log.info("Result = {}", certificateDto);
        return ResponseEntity.ok(certificateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePriceById(@PathVariable @Positive long id,
                                                @RequestBody @Valid GiftCertificatePriceUpdateDto priceUpdateDto) {
        log.info("PATCH request to /v1/gift-certificates/{id} with id = {}, priceUpdateDto = {}", id, priceUpdateDto);
        certificateFacade.updatePriceById(id, priceUpdateDto);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive long id) {
        log.info("DELETE request to /v1/gift-certificates/{id} with id = {}", id);
        certificateFacade.deleteById(id);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }
}
