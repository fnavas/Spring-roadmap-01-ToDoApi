package com.fnavas.todoapi.api;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;
import com.fnavas.todoapi.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = "application/json")
@CrossOrigin(origins = "*")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskRestController implements ApiTaskController {

    private final TaskService taskService;

    @Override
    @GetMapping("/tasks")
    public ResponseEntity<Page<TaskDto>> getAllTasks(
            @ParameterObject TaskFilter taskFilter
    ) {
        Page<TaskDto> taskDtos = taskService.findAll(taskFilter);
        return ResponseEntity.ok(taskDtos);
    }

    @Override
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable @Positive Long id) {
        log.debug("[getTaskById] id={}", id);
        TaskDto taskDto = taskService.findById(id);
        return ResponseEntity.ok(taskDto);
    }

    @Override
    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        log.debug("[createTask] {}", taskDto);
        TaskDto createdTask = taskService.createTask(taskDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.id())
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @Override
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable @Positive Long id, @Valid @RequestBody TaskDto taskDto) {
        log.debug("[updateTask] id={}, body={}", id, taskDto);
        TaskDto updatedTaskDto = taskService.updateTaskById(id, taskDto);
        return ResponseEntity.ok(updatedTaskDto);
    }

    @Override
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @Positive Long id) {
        log.debug("[deleteTask] id={}", id);
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
