package com.fnavas.ToDoApp.mapper;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    @Mapping(target = "completed", defaultValue = "false")
    Task toEntity(TaskDto taskDto);
}
