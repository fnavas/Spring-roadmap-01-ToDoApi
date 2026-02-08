package com.fnavas.ToDoApi.service;

import com.fnavas.ToDoApi.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto findById(Long id);
    List<TaskDto> findByCompleted(Boolean completed);
    List<TaskDto> findAll();
    List<TaskDto> findByTitleContainingIgnoreCase(String title);
    List<TaskDto> findByDescriptionContainingIgnoreCase(String description);
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTaskById(Long id,TaskDto taskDto);
    void deleteTaskById(Long id);
}
