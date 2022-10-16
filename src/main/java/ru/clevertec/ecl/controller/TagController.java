package ru.clevertec.ecl.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.facade.TagFacade;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tags")
public class TagController {

    private final TagFacade tagFacade;

    @GetMapping
    public Page<TagDto> findAll(Pageable pageable) {
        log.info("GET request to /v1/tags with params: pageable = {}", pageable);
        return tagFacade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable long id) {
        return tagFacade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody @Valid TagPutDto newTag) {
        return tagFacade.create(newTag);
    }

    @PutMapping("/{id}")
    public TagDto updateById(@PathVariable long id,
                             @RequestBody @Valid TagPutDto putDto) {
        return tagFacade.updateById(id, putDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        tagFacade.deleteById(id);
    }
}
