package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.entity.Task;
import com.fnavas.todoapi.exception.TaskNotFoundException;
import com.fnavas.todoapi.mapper.TaskMapper;
import com.fnavas.todoapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

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
        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findAll(String title) {
        log.info("[findAll]-Finding all tasks");
        Specification<Task> spec = Specification.where((root, query, cb) -> cb.conjunction());
        if (title != null && !title.isBlank()) {
            log.info("[findAll]-Finding all tasks with title containing ignore case");
            log.debug("[findAll]-Finding all tasks with title containing ignore case {}", title);
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        List<Task> tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskDto> findByDescriptionContainingIgnoreCase(String description) {
        log.info("[findByDescriptionContainingIgnoreCase]-Finding tasks by description containing ignore case");
        log.debug("[findByDescriptionContainingIgnoreCase]-Finding tasks by description containing ignore case {}", description);
        List<Task> tasks = taskRepository.findByDescriptionContainingIgnoreCase(description);
        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
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
        log.info("[updateTaskById]-Finding task by id");
        log.debug("[updateTaskById]-Finding task by id {}", id);
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        task.setDescription(taskDto.description());
        task.setCompleted(taskDto.completed());
        task.setTitle(taskDto.title());
        log.info("[updateTaskById]-Updating task by id");
        log.debug("[updateTaskById]-Updating task by id {}", id);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTaskById(Long id) {
        log.info("[deleteTaskById]-Finding task by id");
        log.debug("[deleteTaskById]-Finding task by id {}", id);
        taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        log.info("[deleteTaskById]-Deleting task by id");
        log.debug("[deleteTaskById]-Deleting task by id {}", id);
        taskRepository.deleteById(id);
    }
}
