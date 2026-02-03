package com.fnavas.ToDoApi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.LocalDate;


public record TaskDto (
        Long id,
        @NotBlank(message = "Title is mandatory")
        String title,
        String description,
        Boolean completed,
        LocalDate created
){ }
