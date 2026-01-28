package com.fnavas.ToDoApp.repository;

import com.fnavas.ToDoApp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
