package com.fnavas.ToDoApi.service;

import com.fnavas.ToDoApi.dto.TaskDto;
import com.fnavas.ToDoApi.entity.Task;
import com.fnavas.ToDoApi.exception.TaskNotFoundException;
import com.fnavas.ToDoApi.mapper.TaskMapper;
import com.fnavas.ToDoApi.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDto findById(Long id) {
        log.info("[findById]-Finding task by id");
        log.debug("[findById]-Finding task by id {}", id);
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> findByCompleted(Boolean completed) {
        log.info("[findByCompleted]-Finding tasks by completed");
        log.debug("[findByCompleted]-Finding tasks by completed {}", completed);
        List<Task> tasks = taskRepository.findByCompleted(completed);
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public List<TaskDto> findAll() {
        log.info("[findAll]-Finding all tasks");
        List<Task> tasks = taskRepository.findAll();
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByTitleContainingIgnoreCase(String title) {
        log.info("[findByTitleContainingIgnoreCase]-Finding tasks by title containing ignore case");
        log.debug("[findByTitleContainingIgnoreCase]-Finding tasks by title containing ignore case {}", title);
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public List<TaskDto> findByDescriptionContainingIgnoreCase(String description) {
        log.info("[findByDescriptionContainingIgnoreCase]-Finding tasks by description containing ignore case");
        log.debug("[findByDescriptionContainingIgnoreCase]-Finding tasks by description containing ignore case {}", description);
        List<Task> tasks = taskRepository.findByDescriptionContainingIgnoreCase(description);
        List<TaskDto> taskDtos = tasks.stream()
                .map(taskMapper::toDto)
                .toList();
        return taskDtos;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        log.info("[createTask]-Creating task");
        log.debug("[createTask]-Creating task {}", taskDto);
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        log.info("[updateTaskById]-Updating task by id");
        log.debug("[updateTaskById]-Updating task by id {} with data {}", id, taskDto);
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        task.setDescription(taskDto.description());
        task.setCompleted(taskDto.completed());
        task.setTitle(taskDto.title());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTaskById(Long id) {
        log.info("[deleteTaskById]-Deleting task by id");
        log.debug("[deleteTaskById]-Deleting task by id {}", id);
        taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        taskRepository.deleteById(id);
    }
}
