package com.fnavas.ToDoApi.api;

import com.fnavas.ToDoApi.dto.TaskDto;
import com.fnavas.ToDoApi.entity.Task;
import com.fnavas.ToDoApi.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void contextLoads() {
        // This test will simply check if the application context loads successfully.
    }

    @Test
    void getAllTasks_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
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
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[0].description").value("description1"))
                .andExpect(jsonPath("$[1].title").value("title2"))
                .andExpect(jsonPath("$[1].description").value("description2"));
    }

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
                .andExpect(status().isNotFound());
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

        mockMvc.perform(get("/api/v1/tasks/search-by-title/{title}", "title")
                .param("title", "TITLE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByTitleContainingIgnoreCase_notMatch_shouldReturnEmptyList() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        Task task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/v1/tasks/search-by-title/{title}", "nonexistent")
                .param("title", "NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
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

        mockMvc.perform(get("/api/v1/tasks/search-by-description/{description}", "description")
                .param("description", "DESCRIPTION")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByDescriptionContainingIgnoreCase_notMatch_shouldReturnEmptyList() throws Exception {
        Task task1 = new Task();
        task1.setTitle("title1");
        task1.setDescription("description1");

        Task task2 = new Task();
        task2.setTitle("title2");
        task2.setDescription("description2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/api/v1/tasks/search-by-description/{description}", "nonexistent")
                .param("description", "NONEXISTENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void createTask_shouldCreateTask() throws Exception{
        TaskDto taskDto = new TaskDto(null, "title", "description", null, null);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"));

        assert(taskRepository.findAll().size() == 1);
    }

    @Test
    void createTask_shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        TaskDto taskDto = new TaskDto(null, null, "description", null, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTaskById_shouldDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");
        Task savedTask = taskRepository.save(task);

        mockMvc.perform(delete("/api/v1/tasks/{id}", savedTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assert(taskRepository.findById(savedTask.getId()).isEmpty());
    }

    @Test
    void deleteTaskById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
