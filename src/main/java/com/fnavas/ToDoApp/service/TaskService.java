package com.fnavas.ToDoApp.service;

import com.fnavas.ToDoApp.entity.Task;

import java.util.List;

public interface TaskService {
    public Task findById(Long id);
    public List<Task> findByStatus(String status);
    public List<Task> findAll();
    public Task createTask(Task task);
    public Task updateTaskById(Long id,Task task);
    public void deleteTaskById(Long id);
}
