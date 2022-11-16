package ru.clevertec.ecl.controller;

import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.request.TagCreateDto;
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
    public ResponseEntity<Page<TagDto>> findAll(Pageable pageable) {
        log.info("GET request to /v1/tags with params: pageable = {}", pageable);
        return ResponseEntity.ok(tagFacade.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable @Positive long id) {
        return ResponseEntity.ok(tagFacade.findById(id));
    }

    @ApiOperation("Find the most used tag by the user that spent the most money")
    @GetMapping("/most-used-by-valuable-user")
    public ResponseEntity<TagOfUserDto> findMostValuableTag() {
        return ResponseEntity.ok(tagFacade.findMostValuableTag());
    }

    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody @Valid TagCreateDto createDto) {
        log.info("POST request to /v1/tags with createDto = {}", createDto);
        TagDto tagDto = tagFacade.create(createDto);
        log.info("Result = {}", tagDto);
        return new ResponseEntity<>(tagDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateById(@PathVariable @Positive long id,
                                             @RequestBody @Valid TagPutDto putDto) {
        return ResponseEntity.ok(tagFacade.updateById(id, putDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @Positive long id) {
        tagFacade.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
