package ru.clevertec.ecl.service;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<Dto, CreateDto, Id> {

    Page<Dto> findAll(Pageable pageable);

    Dto findById(Id id);

    Dto create(CreateDto createDto);

    Dto updateById(Id id, Map<String, Object> newFieldValues);

    void deleteById(Id id);
}
