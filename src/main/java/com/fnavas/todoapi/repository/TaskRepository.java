package com.fnavas.todoapi.repository;

import com.fnavas.todoapi.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByCompleted(Boolean completed);
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Task> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT t FROM Task t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Task> findByDescriptionContainingIgnoreCase(String description);
}
