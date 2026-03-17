package com.fnavas.todoapi.dto;

import com.fnavas.todoapi.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskDto (
        Long id,
        @NotBlank(message = "Title is mandatory")
        String title,
        @Size(max = 255, message = "The description is too long. Max 255 characters")
        String description,
        Boolean completed,
        Priority priority,
        @FutureOrPresent(message = "Due date must be today or in the future")
        LocalDate dueDate,
        LocalDate created
){ }
