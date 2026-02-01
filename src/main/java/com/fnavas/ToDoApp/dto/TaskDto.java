package com.fnavas.ToDoApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.LocalDate;

@Data
public class TaskDto {
    Long id;
    @NotBlank(message = "Title is mandatory")
    String title;
    String description;
    Boolean completed;
    LocalDate created;

}
