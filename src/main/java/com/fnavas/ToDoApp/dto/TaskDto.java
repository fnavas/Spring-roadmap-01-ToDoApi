package com.fnavas.ToDoApp.dto;

import lombok.Builder;
import lombok.Data;


import java.time.LocalDate;

@Data
public class TaskDto {
    Long id;
    String title;
    String description;
    Boolean completed;
    LocalDate created;

}
