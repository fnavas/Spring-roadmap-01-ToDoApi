package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;
import com.fnavas.todoapi.entity.Task;
import com.fnavas.todoapi.exception.TaskNotFoundException;
import com.fnavas.todoapi.mapper.TaskMapper;
import com.fnavas.todoapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private static final Set<String> SORTABLE_FIELDS = Set.of(
            "id", "title", "created", "completed", "priority", "dueDate");

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDto findById(Long id) {
        log.info("[findById] id={}", id);
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public Page<TaskDto> findAll(TaskFilter filter) {
        log.info("[findAll] page={}, size={}, sortBy={}, sortDir={}", filter.getPage(), filter.getSize(), filter.getSortBy(), filter.getSortDir());
        if (!SORTABLE_FIELDS.contains(filter.getSortBy())) {
            throw new IllegalArgumentException("Invalid sortBy field: " + filter.getSortBy()
                    + ". Allowed values: " + SORTABLE_FIELDS);
        }
        Specification<Task> spec = (root, query, cb) -> cb.conjunction();
        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            log.info("[findAll] filter title={}", filter.getTitle());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
        }

        if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
            log.info("[findAll] filter description={}", filter.getDescription());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
        }

        if (filter.getCompleted() != null) {
            log.info("[findAll] filter completed={}", filter.getCompleted());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("completed"), filter.getCompleted()));
        }

        if (filter.getPriority() != null) {
            log.info("[findAll] filter priority={}", filter.getPriority());
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("priority"), filter.getPriority()));
        }

        Sort sort = filter.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        return taskRepository.findAll(spec, pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional
    public TaskDto createTask(TaskDto taskDto) {
        log.info("[createTask] title={}", taskDto.title());
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        log.info("[updateTaskById] id={}", id);
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with id " + id + " not found"));
        if (taskDto.title() != null) task.setTitle(taskDto.title());
        if (taskDto.description() != null) task.setDescription(taskDto.description());
        if (taskDto.completed() != null) task.setCompleted(taskDto.completed());
        if (taskDto.priority() != null) task.setPriority(taskDto.priority());
        if (taskDto.dueDate() != null) task.setDueDate(taskDto.dueDate());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        log.info("[deleteTaskById] id={}", id);
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
