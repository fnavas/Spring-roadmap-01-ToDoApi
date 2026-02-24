package com.fnavas.todoapi.dto;

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

}
