package com.fnavas.ToDoApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    private String description;
    @Column(nullable = false)
    private Boolean completed = Boolean.FALSE;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate created;
}
