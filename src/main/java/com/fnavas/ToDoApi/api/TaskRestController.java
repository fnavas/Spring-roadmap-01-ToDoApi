package com.fnavas.ToDoApi.api;

import com.fnavas.ToDoApi.dto.TaskDto;
import com.fnavas.ToDoApi.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskRestController implements ApiTaskController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        log.info("[getAllTasks]-Getting all tasks");
        List<TaskDto> taskDtos = taskService.findAll();
        return ResponseEntity.ok(taskDtos);
    }

    @Override
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        log.info("[getTaskById]-Getting task by id");
        log.debug("[getTaskById]-Getting task by id {}", id);
        TaskDto taskDto = taskService.findById(id);
        return ResponseEntity.ok(taskDto);
    }

    @Override
    @GetMapping("/tasks/completed")
    public ResponseEntity<List<TaskDto>> getCompletedTasks() {
        log.info("[getCompletedTasks]-Getting completed tasks");
        List<TaskDto> completedTasks = taskService.findByCompleted(true);
        return ResponseEntity.ok(completedTasks);
    }

    @Override
    @GetMapping("/tasks/pending")
    public ResponseEntity<List<TaskDto>> getPendingTasks() {
        log.info("[getPendingTasks]-Getting pending tasks");
        List<TaskDto> pendingTasks = taskService.findByCompleted(false);
        return ResponseEntity.ok(pendingTasks);
    }

    @Override
    @GetMapping("/tasks/search-by-title/{title}")
    public ResponseEntity<List<TaskDto>> getTasksByTitle(@PathVariable String title) {
        log.info("[getTasksByTitle]-Getting tasks by title");
        log.debug("[getTasksByTitle]-Getting tasks by title {}", title);
        List<TaskDto> taskDtos = taskService.findByTitleContainingIgnoreCase(title);
        return ResponseEntity.ok(taskDtos);
    }

    @Override
    @GetMapping("/tasks/search-by-description/{description}")
    public ResponseEntity<List<TaskDto>> getTasksByDescription(@PathVariable String description) {
        log.info("[getTasksByDescription]-Getting tasks by description");
        log.debug("[getTasksByDescription]-Getting tasks by description {}", description);
        List<TaskDto> taskDtos = taskService.findByDescriptionContainingIgnoreCase(description);
        return ResponseEntity.ok(taskDtos);
    }

    @Override
    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        log.info("[createTask]-Creating task");
        log.debug("[createTask]-Creating task {}", taskDto);
        TaskDto createdTask = taskService.createTask(taskDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/tasks/{id}")
                .buildAndExpand(createdTask.id())
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @Override
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        log.info("[updateTask]-Updating task");
        log.debug("[updateTask]-Updating task {}", taskDto);
        TaskDto updatedTaskDto = taskService.updateTaskById(id, taskDto);
        return ResponseEntity.ok(updatedTaskDto);
    }

    @Override
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        log.info("[deleteTask]-Deleting task by id");
        log.debug("[deleteTask]-Deleting task by id {}", id);
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
