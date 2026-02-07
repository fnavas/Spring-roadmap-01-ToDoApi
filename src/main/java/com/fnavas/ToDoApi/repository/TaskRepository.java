package com.fnavas.ToDoApi.repository;

import com.fnavas.ToDoApi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByCompleted(Boolean completed);
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    public List<Task> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT t FROM Task t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    public List<Task> findByDescriptionContainingIgnoreCase(String description);
}
