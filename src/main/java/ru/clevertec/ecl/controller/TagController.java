package ru.clevertec.ecl.controller;

import java.util.Map;
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
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagRequestDto;
import ru.clevertec.ecl.facade.TagFacade;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagFacade facade;

    @GetMapping
    public Page<TagDto> findAll(Pageable pageable) {
        log.info("GET request to /api/v1/tags with params: pageable = {}", pageable);
        return facade.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TagDto findById(@PathVariable long id) {
        return facade.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody TagRequestDto newItem) {
        return facade.create(newItem);
    }

    @PatchMapping("/{id}")
    public TagDto updateById(@PathVariable long id, @RequestBody Map<String, Object> newFieldValues) {
        return facade.updateById(id, newFieldValues);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        facade.deleteById(id);
    }
}
