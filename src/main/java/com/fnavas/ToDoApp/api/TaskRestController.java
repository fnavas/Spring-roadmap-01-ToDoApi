package com.fnavas.ToDoApp.api;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import com.fnavas.ToDoApp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TaskRestController {

    private final TaskService taskService;

    public TaskRestController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> taskDtos = taskService.findAll();
        return ResponseEntity.ok(taskDtos);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = taskService.findById(id);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        TaskDto createdTask = taskService.createTask(taskDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/tasks/{id}")
                .buildAndExpand(createdTask.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
