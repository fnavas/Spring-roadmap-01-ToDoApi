package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;
import org.springframework.data.domain.Page;

public interface TaskService {
    TaskDto findById(Long id);
    Page<TaskDto> findAll(TaskFilter taskFilter);
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTaskById(Long id,TaskDto taskDto);
    void deleteTaskById(Long id);
}
