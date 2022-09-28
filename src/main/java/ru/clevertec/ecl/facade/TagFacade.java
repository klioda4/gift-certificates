package ru.clevertec.ecl.facade;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.ecl.dto.TagDto;
import ru.clevertec.ecl.dto.request.TagRequestDto;

public interface TagFacade {

    TagDto findById(long id);

    TagDto create(TagRequestDto createDto);

    TagDto updateById(long id, Map<String, Object> newFieldValues);

    void deleteById(long id);

    Page<TagDto> findAll(Pageable pageable);
}
