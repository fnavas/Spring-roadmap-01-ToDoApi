package com.fnavas.todoapi.api;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import com.fnavas.todoapi.exception.ErrorResponse;

import java.util.List;

public interface ApiTaskController {
    @Operation(summary = "List Tasks with dynamic filter", description = "Retrieve a list of all tasks with optional filtering by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks"),
    })
    ResponseEntity<List<TaskDto>> getAllTasks(TaskFilter filter);

    @Operation(summary = "Get Task by ID", description = "Retrieve a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the task"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<TaskDto> getTaskById(Long id);

    @Operation(summary = "Create Task", description = "Create a new task")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\"title\":\"Example title\",\"description\":\"Example description\"}"
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the task")
    })
    ResponseEntity<TaskDto> createTask(TaskDto taskDto);

    @Operation(summary = "Update Task", description = "Update an existing task by its ID")
    @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\"title\":\"Example title\"" +
                                    ",\"description\":\"Example description\"" +
                                    ",\"completed\":true}"
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the task"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<TaskDto> updateTask(Long id, TaskDto taskDto);

    @Operation(summary = "Delete Task", description = "Delete a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the task"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity deleteTask(Long id);
}
