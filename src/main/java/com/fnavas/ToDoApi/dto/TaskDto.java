package com.fnavas.ToDoApi.dto;

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
        LocalDate created
){ }
