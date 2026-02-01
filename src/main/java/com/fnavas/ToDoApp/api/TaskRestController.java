package com.fnavas.ToDoApp.api;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import com.fnavas.ToDoApp.service.TaskService;
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

public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        log.info("[getAllTasks]-Getting all tasks");
        List<TaskDto> taskDtos = taskService.findAll();
        return ResponseEntity.ok(taskDtos);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        log.info("[getTaskById]-Getting task by id");
        log.debug("[getTaskById]-Getting task by id {}", id);
        TaskDto taskDto = taskService.findById(id);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        log.info("[createTask]-Creating task");
        log.debug("[createTask]-Creating task {}", taskDto);
        TaskDto createdTask = taskService.createTask(taskDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/tasks/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        log.info("[updateTask]-Updating task");
        log.debug("[updateTask]-Updating task {}", taskDto);
        TaskDto updatedTaskDto = taskService.updateTaskById(id, taskDto);
        return ResponseEntity.ok(updatedTaskDto);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        log.info("[deleteTask]-Deleting task by id");
        log.debug("[deleteTask]-Deleting task by id {}", id);
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}
