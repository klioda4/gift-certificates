package ru.clevertec.ecl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.dto.request.CommitLogStatusUpdateDto;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;
import ru.clevertec.ecl.service.CommitLogService;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/commit-log")
public class CommitLogController {

    private final CommitLogService commitLogService;

    @GetMapping("/{id}")
    public ResponseEntity<CommitLogDto> findById(@PathVariable long id) {
        return ResponseEntity.ok(commitLogService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CommitLogDto> create(@RequestBody CommitLogDto commitLogDto,
                                               boolean updateSequences) {
        log.info("POST request to /commit-log with params: commitLogDto={}, updateSequences={}", commitLogDto,
                 updateSequences);
        CommitLogDto commitLog = commitLogService.create(commitLogDto, updateSequences);
        log.info("Result: {}", commitLog);
        return new ResponseEntity<>(commitLog,
                                    HttpStatus.CREATED);
    }

    @PostMapping("/{id}/apply")
    public ResponseEntity<Void> apply(@PathVariable long id) {
        log.info("POST request to /commit-log/{}/apply", id);
        commitLogService.apply(id);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/decline")
    public ResponseEntity<Void> decline(@PathVariable long id) {
        log.info("POST request to /commit-log/{}/decline", id);
        commitLogService.decline(id);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable long id,
                                             @RequestBody CommitLogStatusUpdateDto statusUpdateDto) {
        log.info("PATCH request to /commit-log/{}/status with params: statusUpdateDto={}", id, statusUpdateDto);
        commitLogService.updateStatus(id, statusUpdateDto);
        log.info("Request has been completed.");
        return ResponseEntity.noContent().build();
    }

    @ApiIgnore
    @GetMapping("/sequence/{entityName}/next")
    public SequenceDto getNextSequence(@PathVariable String entityName) {
        log.debug("GET request to /commit-log/sequence/{}/next", entityName);
        SequenceDto nextSequence = commitLogService.getNextSequence(entityName);
        log.debug("Result: {}", nextSequence);
        return nextSequence;
    }

    @ApiIgnore
    @GetMapping("/sequence/{entityName}/current")
    public SequenceDto getCurrentSequence(@PathVariable String entityName) {
        log.debug("GET request to /commit-log/sequence/{}/current", entityName);
        SequenceDto nextSequence = commitLogService.getCurrentSequence(entityName);
        log.debug("Result: {}", nextSequence);
        return nextSequence;
    }
}
