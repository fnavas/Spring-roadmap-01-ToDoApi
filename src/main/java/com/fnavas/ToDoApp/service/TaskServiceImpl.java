package com.fnavas.ToDoApp.service;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import com.fnavas.ToDoApp.mapper.TaskMapper;
import com.fnavas.ToDoApp.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Task task = taskRepository.findById(id).orElse(null);
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
    public TaskDto createTask(TaskDto taskDto) {
        log.info("[createTask]-Creating task");
        log.debug("[createTask]-Creating task {}", taskDto);
        Task task = taskMapper.toEntity(taskDto);
        TaskDto savedTaskDto = taskMapper.toDto(taskRepository.save(task));
        return savedTaskDto;
    }

    @Override
    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        log.info("[updateTaskById]-Updating task by id");
        log.debug("[updateTaskById]-Updating task by id {} with data {}", id, taskDto);
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
        log.info("[deleteTaskById]-Deleting task by id");
        log.debug("[deleteTaskById]-Deleting task by id {}", id);
        taskRepository.deleteById(id);
    }
}
