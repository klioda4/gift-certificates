package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.GiftCertificateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateCreateDto;
import ru.clevertec.ecl.dto.request.GiftCertificateFilterDto;
import ru.clevertec.ecl.dto.request.GiftCertificateUpdateDto;
import ru.clevertec.ecl.facade.GiftCertificateFacade;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateFacade facade;

    @GetMapping
    public Page<GiftCertificateDto> findAllByFilter(Pageable pageable, GiftCertificateFilterDto filterDto) {
        log.info("GET request to /api/v1/gift-certificates with params: pageable = {}, filterDto = {}", pageable,
            filterDto);
        return facade.findAllByFilter(pageable, filterDto);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable long id) {
        return facade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody GiftCertificateCreateDto newItem) {
        return facade.create(newItem);
    }

    @PatchMapping("/{id}")
    public GiftCertificateDto updateById(@PathVariable long id, @RequestBody GiftCertificateUpdateDto updateDto) {
        return facade.updateById(id, updateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        facade.deleteById(id);
    }
}
