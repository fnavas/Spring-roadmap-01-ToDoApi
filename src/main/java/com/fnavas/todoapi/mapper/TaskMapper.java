package com.fnavas.todoapi.mapper;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    @Mapping(target = "completed", defaultValue = "false")
    @Mapping(target = "priority", defaultValue = "MEDIUM")
    Task toEntity(TaskDto taskDto);
}
