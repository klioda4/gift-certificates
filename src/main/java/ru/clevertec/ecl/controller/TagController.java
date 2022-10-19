package ru.clevertec.ecl.controller;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.dto.response.TagOfUserDto;
import ru.clevertec.ecl.facade.TagFacade;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/tags")
@Validated
public class TagController {

    private final TagFacade tagFacade;

    @GetMapping
    public Page<TagDto> findAll(Pageable pageable) {
        log.info("GET request to /v1/tags with params: pageable = {}", pageable);
        return tagFacade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable @Positive long id) {
        return tagFacade.findById(id);
    }

    @ApiOperation("Find the most used tag by the user that spent the most money")
    @GetMapping("/most-used-by-valuable-user")
    public TagOfUserDto findMostValuableTag() {
        return tagFacade.findMostValuableTag();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody @Valid TagPutDto newTag) {
        return tagFacade.create(newTag);
    }

    @PutMapping("/{id}")
    public TagDto updateById(@PathVariable @Positive long id,
                             @RequestBody @Valid TagPutDto putDto) {
        return tagFacade.updateById(id, putDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Positive long id) {
        tagFacade.deleteById(id);
    }
}
