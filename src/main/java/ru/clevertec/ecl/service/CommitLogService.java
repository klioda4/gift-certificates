package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.request.CommitLogStatusUpdateDto;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.dto.response.SequenceDto;

public interface CommitLogService {

    CommitLogDto findById(long id);

    CommitLogDto create(CommitLogDto commitLogDto, boolean isCurrentNode);

    void apply(long id);

    void decline(long id);

    void updateStatus(long id, CommitLogStatusUpdateDto statusUpdateDto);

    SequenceDto getCurrentSequence(String entityName);

    SequenceDto getNextSequence(String entityName);
}
