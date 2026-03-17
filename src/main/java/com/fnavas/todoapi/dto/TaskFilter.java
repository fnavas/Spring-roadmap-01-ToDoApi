package com.fnavas.todoapi.dto;

import com.fnavas.todoapi.entity.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TaskFilter {
    @Schema(description = "Filter tasks by title. This will return tasks that contain the specified string in their title.",
    example = "Example", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String title;
    @Schema(description = "Filter tasks by description. This will return tasks that contain the specified string in their description.",
            example = "Example description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @Schema(description = "Filter tasks by completion status. This will return tasks that are either completed or pending based on the specified value.",
            example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean completed;
    @Schema(description = "Filter tasks by priority level: LOW, MEDIUM or HIGH.",
            example = "HIGH", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Priority priority;

    @Schema(description = "Page number (0-based).", example = "0", defaultValue = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int page = 0;
    @Schema(description = "Number of tasks per page.", example = "10", defaultValue = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private int size = 10;
    @Schema(description = "Field to sort by: id, title, created, completed.", example = "created", defaultValue = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortBy = "id";
    @Schema(description = "Sort direction: asc or desc.", example = "asc", defaultValue = "asc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortDir = "asc";
}
