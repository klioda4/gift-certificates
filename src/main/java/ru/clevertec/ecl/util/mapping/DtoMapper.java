package ru.clevertec.ecl.util.mapping;

public interface DtoMapper<Entity, Dto, CreateDto> {

    Dto mapToDto(Entity dto);

    Entity mapToEntity(Dto dto);

    Entity mapCreateDtoToEntity(CreateDto dto);
}
