package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto findById(Long id);
    List<TaskDto> findByCompleted(Boolean completed);
    List<TaskDto> findAll(String title);
    List<TaskDto> findByDescriptionContainingIgnoreCase(String description);
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTaskById(Long id,TaskDto taskDto);
    void deleteTaskById(Long id);
}
