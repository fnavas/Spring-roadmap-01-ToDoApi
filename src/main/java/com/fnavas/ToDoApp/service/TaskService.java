package com.fnavas.ToDoApp.service;

import com.fnavas.ToDoApp.dto.TaskDto;

import java.util.List;

public interface TaskService {
    public TaskDto findById(Long id);
    public List<TaskDto> findByCompleted(Boolean completed);
    public List<TaskDto> findAll();
    public TaskDto createTask(TaskDto taskDto);
    public TaskDto updateTaskById(Long id,TaskDto taskDto);
    public void deleteTaskById(Long id);
}
