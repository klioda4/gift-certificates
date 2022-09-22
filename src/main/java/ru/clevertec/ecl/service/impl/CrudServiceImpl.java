package ru.clevertec.ecl.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.exception.ObjectNotFoundException;
import ru.clevertec.ecl.service.CrudService;
import ru.clevertec.ecl.util.mapping.DtoMapper;

@RequiredArgsConstructor
public class CrudServiceImpl<Entity, Dto, CreateDto, Id> implements CrudService<Dto, CreateDto, Id> {

    private final JpaRepository<Entity, Id> repository;
    private final DtoMapper<Entity, Dto, CreateDto> mapper;

    @Override
    public Page<Dto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::mapToDto);
    }

    @Override
    public Dto findById(Id id) throws ObjectNotFoundException {
        return mapper.mapToDto(
            findEntityByIdOrThrow(id));
    }

    @Override
    public Dto create(CreateDto createDto) {
        Entity newEntity = mapper.mapCreateDtoToEntity(createDto);
        return mapper.mapToDto(
            repository.save(newEntity));
    }

    @Override
    @Transactional
    public Dto updateById(Id id, Map<String, Object> newFieldValues) {
        Entity entityToUpdate = findEntityByIdOrThrow(id);
        populateFields(entityToUpdate, newFieldValues);
        return mapper.mapToDto(
            repository.save(entityToUpdate));
    }

    @Override
    @Transactional
    public void deleteById(Id id) throws ObjectNotFoundException {
        Entity entityToDelete = findEntityByIdOrThrow(id);
        repository.delete(entityToDelete);
    }

    private Entity findEntityByIdOrThrow(Id id) throws ObjectNotFoundException {
        return repository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("", "id", id));
    }

    private void populateFields(Entity entity, Map<String, Object> newFieldValues) {
        try {
            BeanUtils.populate(entity, newFieldValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BadRequestException(e);
        }
    }
}
