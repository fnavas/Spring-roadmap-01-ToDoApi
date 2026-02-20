package com.fnavas.todoapi.repository;

import com.fnavas.todoapi.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
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
    void findAll_withTitle_shouldReturnFilteredTasks() {
        String titleFilter = "ONE";
        Specification<Task> spec = Specification.where((root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + titleFilter.toLowerCase() + "%"));

        List<Task> results = taskRepository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Task One", results.get(0).getTitle());
    }

    @Test
    void findAll_withDescription_shouldReturnFilteredTasks() {
        String descFilter = "UNIT TESTS";
        Specification<Task> spec = Specification.where((root, query, cb) ->
                cb.like(cb.lower(root.get("description")), "%" + descFilter.toLowerCase() + "%"));

        List<Task> results = taskRepository.findAll(spec);

        assertEquals(1, results.size());
        assertEquals("Task Two", results.get(0).getTitle());
    }

    @Test
    void findAll_withNoFilters_shouldReturnAllTasks() {
        Specification<Task> spec = Specification.where((root, query, cb) -> cb.conjunction());

        List<Task> results = taskRepository.findAll(spec);

        assertEquals(2, results.size());
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
    void createTask_withTitleNull_shouldThrowsException() {
        Task task = new Task();
        task.setDescription("Task without title");
        task.setCompleted(false);

        assertThrows(DataIntegrityViolationException.class, () -> {taskRepository.save(task);});
    }

    @Test
    void updateTask_withCreatedDate_shouldNotChange() {
        Task task = taskRepository.findAll().get(0);
        LocalDate originalDate = task.getCreated();
        Long id = task.getId();
        task.setCreated(originalDate.plusDays(1));
        taskRepository.saveAndFlush(task);
        testEntityManager.clear();

        Task taskFromDB = taskRepository.findById(id).orElseThrow();

        assertEquals(originalDate, taskFromDB.getCreated());
    }
}