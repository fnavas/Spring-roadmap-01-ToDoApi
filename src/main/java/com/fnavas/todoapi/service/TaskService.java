package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;

import java.util.List;

public interface TaskService {
    TaskDto findById(Long id);
    List<TaskDto> findAll(TaskFilter taskFilter);
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTaskById(Long id,TaskDto taskDto);
    void deleteTaskById(Long id);
}
