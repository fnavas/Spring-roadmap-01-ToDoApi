package com.fnavas.todoapi.api;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.entity.Priority;
import com.fnavas.todoapi.entity.Task;
import com.fnavas.todoapi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    // ── Contract: TaskDto schema ─────────────────────────────────────────────

    @Test
    void contract_taskDto_shouldContainAllFieldsWithCorrectTypes() throws Exception {
        Task task = new Task();
        task.setTitle("contract task");
        task.setDescription("desc");
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(1));
        taskRepository.save(task);

        mockMvc.perform(get("/api/v1/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.description").isString())
                .andExpect(jsonPath("$.completed").isBoolean())
                .andExpect(jsonPath("$.priority").isString())
                .andExpect(jsonPath("$.dueDate").isString())
                .andExpect(jsonPath("$.created").isString());
    }

    @Test
    void contract_taskDto_nullableFieldsCanBeNull() throws Exception {
        Task task = new Task();
        task.setTitle("minimal task");
        taskRepository.save(task);

        mockMvc.perform(get("/api/v1/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").isString())
                .andExpect(jsonPath("$.description").doesNotExist())
                .andExpect(jsonPath("$.completed").isBoolean())
                .andExpect(jsonPath("$.priority").isString())
                .andExpect(jsonPath("$.dueDate").doesNotExist())
                .andExpect(jsonPath("$.created").isString());
    }

    // ── Contract: Page<TaskDto> schema ────────────────────────────────────────

    @Test
    void contract_page_shouldContainAllPaginationFields() throws Exception {
        Task task = new Task();
        task.setTitle("page contract");
        taskRepository.save(task);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.number").isNumber())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.first").isBoolean())
                .andExpect(jsonPath("$.last").isBoolean());
    }

    @Test
    void contract_page_taskInsideContentHasCorrectSchema() throws Exception {
        Task task = new Task();
        task.setTitle("nested contract");
        task.setDescription("inside page");
        task.setPriority(Priority.LOW);
        taskRepository.save(task);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").isNumber())
                .andExpect(jsonPath("$.content[0].title").isString())
                .andExpect(jsonPath("$.content[0].description").isString())
                .andExpect(jsonPath("$.content[0].completed").isBoolean())
                .andExpect(jsonPath("$.content[0].priority").isString())
                .andExpect(jsonPath("$.content[0].created").isString());
    }

    // ── Contract: ErrorResponse schema ────────────────────────────────────────

    @Test
    void contract_errorResponse_notFound_shouldContainAllFields() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.timestamp").isString());
    }

    @Test
    void contract_errorResponse_badRequest_shouldContainAllFields() throws Exception {
        TaskDto invalid = new TaskDto(null, null, "no title", null, null, null, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").isNumber())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.timestamp").isString());
    }

    @Test
    void contract_errorResponse_invalidSortBy_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                        .param("sortBy", "nonExistentField"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.timestamp").isString());
    }

    // ── GET /tasks ─────────────────────────────────────────────────────────────

    @Test
    void getAllTasks_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        Task task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].title").value("title1"))
                .andExpect(jsonPath("$.content[0].description").value("description1"))
                .andExpect(jsonPath("$.content[1].title").value("title2"))
                .andExpect(jsonPath("$.content[1].description").value("description2"));
    }

    @Test
    void findByTitleContainingIgnoreCase_shouldReturnTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        Task task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/v1/tasks")
                .param("title", "TITLE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void findByTitleContainingIgnoreCase_notMatch_shouldReturnEmptyList() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        taskRepository.save(task1);

        mockMvc.perform(get("/api/v1/tasks")
                .param("title", "NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void findByDescriptionContainingIgnoreCase_shouldReturnTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        Task task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/v1/tasks")
                .param("description", "DESCRIPTION")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void findByDescriptionContainingIgnoreCase_notMatch_shouldReturnEmptyList() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        taskRepository.save(task1);

        mockMvc.perform(get("/api/v1/tasks")
                .param("description", "NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    void findByPriority_shouldReturnFilteredTasks() throws Exception {
        Task highTask = new Task();
        highTask.setTitle("High priority task");
        highTask.setPriority(Priority.HIGH);

        Task lowTask = new Task();
        lowTask.setTitle("Low priority task");
        lowTask.setPriority(Priority.LOW);

        taskRepository.save(highTask);
        taskRepository.save(lowTask);

        mockMvc.perform(get("/api/v1/tasks")
                .param("priority", "HIGH")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].priority").value("HIGH"));
    }

    // ── GET /tasks (pagination & sorting) ─────────────────────────────────────

    @Test
    void getAllTasks_withPagination_shouldReturnFirstPage() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskRepository.save(task);
        }

        mockMvc.perform(get("/api/v1/tasks")
                .param("page", "0")
                .param("size", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getAllTasks_withPagination_shouldReturnSecondPage() throws Exception {
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setTitle("task" + i);
            taskRepository.save(task);
        }

        mockMvc.perform(get("/api/v1/tasks")
                .param("page", "1")
                .param("size", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.number").value(1));
    }

    @Test
    void getAllTasks_sortedByTitleDesc_shouldReturnTasksInDescOrder() throws Exception {
        Task taskA = new Task();
        taskA.setTitle("Alpha");
        Task taskB = new Task();
        taskB.setTitle("Beta");
        Task taskC = new Task();
        taskC.setTitle("Gamma");
        taskRepository.save(taskA);
        taskRepository.save(taskB);
        taskRepository.save(taskC);

        mockMvc.perform(get("/api/v1/tasks")
                .param("sortBy", "title")
                .param("sortDir", "desc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Gamma"))
                .andExpect(jsonPath("$.content[1].title").value("Beta"))
                .andExpect(jsonPath("$.content[2].title").value("Alpha"));
    }

    // ── GET /tasks/{id} ────────────────────────────────────────────────────────

    @Test
    void getTaskById_shouldReturnTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(get("/api/v1/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void getTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void getTaskById_withInvalidId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", 0L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ── POST /tasks ────────────────────────────────────────────────────────────

    @Test
    void createTask_shouldCreateTask() throws Exception {
        TaskDto taskDto = new TaskDto(null, "title", "description", null, null, null, null);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));

        assertEquals(1, taskRepository.findAll().size());
    }

    @Test
    void createTask_withPriorityAndDueDate_shouldPersistFields() throws Exception {
        TaskDto taskDto = new TaskDto(null, "urgent task", "desc", null, Priority.HIGH,
                LocalDate.now().plusDays(7), null);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.dueDate").exists());
    }

    @Test
    void createTask_shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        TaskDto taskDto = new TaskDto(null, null, "description", null, null, null, null);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400));
    }

    // ── PUT /tasks/{id} ────────────────────────────────────────────────────────

    @Test
    void updateTask_shouldUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("original title");
        task.setDescription("original description");
        Task savedTask = taskRepository.save(task);

        TaskDto updateDto = new TaskDto(null, "updated title", "updated description", true, Priority.HIGH, null, null);

        mockMvc.perform(put("/api/v1/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.description").value("updated description"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void updateTask_shouldReturnNotFound() throws Exception {
        TaskDto updateDto = new TaskDto(null, "title", "description", false, null, null, null);

        mockMvc.perform(put("/api/v1/tasks/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void updateTask_shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        Task savedTask = taskRepository.save(task);

        TaskDto updateDto = new TaskDto(null, null, "description", false, null, null, null);

        mockMvc.perform(put("/api/v1/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    // ── DELETE /tasks/{id} ─────────────────────────────────────────────────────

    @Test
    void deleteTaskById_shouldDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(delete("/api/v1/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(taskRepository.findById(savedTask.getId()).isEmpty());
    }

    @Test
    void deleteTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404));
    }
}
