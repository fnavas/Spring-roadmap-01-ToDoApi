package com.fnavas.ToDoApi.repository;

import com.fnavas.ToDoApi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByCompleted(Boolean completed);
}
