package ru.clevertec.ecl.util.mapping;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.dto.response.CommitLogDto;
import ru.clevertec.ecl.model.CommitLog;

@Mapper
public interface CommitLogDtoMapper {

    CommitLogDto mapToDto(CommitLog commitLog);

    CommitLog mapToCommitLog(CommitLogDto commitLogDto);
}
