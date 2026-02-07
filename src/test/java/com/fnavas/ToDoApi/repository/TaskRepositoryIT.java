package com.fnavas.ToDoApi.repository;

import com.fnavas.ToDoApi.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryIT {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUP(){
        taskRepository.deleteAll();

        Task task1 = new Task();
        task1.setTitle("Task One");
        task1.setDescription("Learn Spring Boot Testing");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setTitle("Task Two");
        task2.setDescription("Write unit tests for TaskRepository");
        task2.setCompleted(true);

        testEntityManager.persist(task1);
        testEntityManager.persist(task2);
        testEntityManager.flush();
    }

    @Test
    void findByCompleted_shouldReturnsCompletedTasks() {
        List<Task> tasksCompleted = taskRepository.findByCompleted(true);
        List<Task> tasksNotCompleted = taskRepository.findByCompleted(false);

        assertEquals(1, tasksCompleted.size());
        assertEquals("Task Two", tasksCompleted.get(0).getTitle());

        assertEquals(1, tasksNotCompleted.size());
        assertEquals("Task One", tasksNotCompleted.get(0).getTitle());
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnsTasksWithTitleContaining() {
        List<Task> results = taskRepository.findByTitleContainingIgnoreCase("one");

        assertEquals(1, results.size());
        assertEquals("Task One", results.get(0).getTitle());
    }

    @Test
    void findByTitleContainingIgnoreCase_NoMatch() {
        List<Task> results = taskRepository.findByTitleContainingIgnoreCase("NonExisting");

        assertTrue(results.isEmpty());
    }

    @Test
    void findByDescriptionContainingIgnoreCase_shouldReturnsTaskWithDescriptionContaining() {
        List<Task> results = taskRepository.findByDescriptionContainingIgnoreCase("spring");

        assertEquals(1, results.size());
        assertEquals("Learn Spring Boot Testing", results.get(0).getDescription());
    }

    @Test
    void findByDescriptionContainingIgnoreCase_NoMatch() {
        List<Task> results = taskRepository.findByDescriptionContainingIgnoreCase("NonExisting");

        assertTrue(results.isEmpty());
    }

    @Test
    void createTask_withTitleNull_shouldThrowsException() {
        Task task = new Task();
        task.setDescription("Task without title");
        task.setCompleted(false);

        assertThrows(DataIntegrityViolationException.class, () -> {taskRepository.save(task);});
    }

    @Test
    void updateTask_withCreatedDate_shouldNotChange() {
        Task task = taskRepository.findByTitleContainingIgnoreCase("Task One").get(0);
        LocalDate originalDate = task.getCreated();
        Long id = task.getId();
        task.setCreated(originalDate.plusDays(1));
        taskRepository.saveAndFlush(task);
        testEntityManager.clear();

        Task taskFromDB = taskRepository.findById(id).orElseThrow();

        assertEquals(originalDate, taskFromDB.getCreated());
    }
}