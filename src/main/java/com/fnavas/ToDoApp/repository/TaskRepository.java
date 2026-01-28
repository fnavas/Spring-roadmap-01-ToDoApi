package com.fnavas.ToDoApp.repository;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByCompleted(Boolean completed);
}
