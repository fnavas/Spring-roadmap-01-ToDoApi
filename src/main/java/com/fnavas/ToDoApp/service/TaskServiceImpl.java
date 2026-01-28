package com.fnavas.ToDoApp.service;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import com.fnavas.ToDoApp.mapper.TaskMapper;
import com.fnavas.ToDoApp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> findByCompleted(Boolean completed) {
        List<Task> tasks = taskRepository.findByCompleted(completed);
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public List<TaskDto> findAll() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        TaskDto savedTaskDto = taskMapper.toDto(taskRepository.save(task));
        return savedTaskDto;
    }

    @Override
    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            task.setDescription(taskDto.getDescription());
            task.setCompleted(taskDto.getCompleted());
            task.setTitle(taskDto.getTitle());
            return taskMapper.toDto(taskRepository.save(task));
        }
        return null;
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
