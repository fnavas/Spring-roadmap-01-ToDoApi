package com.fnavas.ToDoApi.mapper;

import com.fnavas.ToDoApi.dto.TaskDto;
import com.fnavas.ToDoApi.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    @Mapping(target = "completed", defaultValue = "false")
    Task toEntity(TaskDto taskDto);
}
