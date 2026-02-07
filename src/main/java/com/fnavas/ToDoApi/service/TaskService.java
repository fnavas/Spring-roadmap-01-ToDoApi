package com.fnavas.ToDoApi.service;

import com.fnavas.ToDoApi.dto.TaskDto;

import java.util.List;

public interface TaskService {
    public TaskDto findById(Long id);
    public List<TaskDto> findByCompleted(Boolean completed);
    public List<TaskDto> findAll();
    public List<TaskDto> findByTitleContainingIgnoreCase(String title);
    public List<TaskDto> findByDescriptionContainingIgnoreCase(String description);
    public TaskDto createTask(TaskDto taskDto);
    public TaskDto updateTaskById(Long id,TaskDto taskDto);
    public void deleteTaskById(Long id);
}
