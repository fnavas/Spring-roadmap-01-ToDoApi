package com.fnavas.ToDoApi.api;

import com.fnavas.ToDoApi.dto.TaskDto;
import com.fnavas.ToDoApi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
class TaskRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;


    private TaskDto sampleTaskDto() {
        TaskDto sampleTaskDto = new TaskDto(1L, "title", "description", null, null);
        return sampleTaskDto;
    }

    @Test
    void getAllTasks_shouldReturnTaskList() throws Exception {
        List<TaskDto> taskDtos  = List.of(sampleTaskDto(), sampleTaskDto());
        Mockito.when(taskService.findAll()).thenReturn(taskDtos);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("title"))
                .andExpect(jsonPath("$[0].description").value("description"));

        Mockito.verify(taskService, Mockito.times(1)).findAll();
    }

    @Test
    void getTaskByID_shouldReturnTaskById() throws Exception {
        Long id = 1L;
        TaskDto taskDto = sampleTaskDto();
        Mockito.when(taskService.findById(id)).thenReturn(taskDto);

        mockMvc.perform(get("/api/v1/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("description"));

        Mockito.verify(taskService, Mockito.times(1)).findById(id);
    }

    @Test
    void createTask_shouldCreateTask() throws Exception {
        TaskDto request = new TaskDto(1L, "title", "description", null, null);
        TaskDto response = new TaskDto(null, null, null, null, null);
        Mockito.when(taskService.createTask(any(TaskDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Mockito.verify(taskService, Mockito.times(1)).createTask(any(TaskDto.class));
    }

    @Test
    void createTask_withInvalidData_shouldReturnBadRequest() throws Exception {
        TaskDto request = new TaskDto(null, null,"description", null, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).createTask(any(TaskDto.class));
    }

    @Test
    void updateTask_shouldUpdateTask() throws Exception {
        Long id = 1L;
        TaskDto request = new TaskDto(null, "title", "description", null, null);

        TaskDto response = new TaskDto(null, null, null, null, null);
        Mockito.when(taskService.updateTaskById(eq(id), any(TaskDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(taskService, Mockito.times(1)).updateTaskById(eq(id), any(TaskDto.class));
    }

    @Test
    void updateTask_withInvalidData_shouldReturnBadRequest() throws Exception {
        Long id = 1L;
        TaskDto request = new TaskDto(null, null,"description", null, null);

        mockMvc.perform(put("/api/v1/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(taskService, Mockito.never()).updateTaskById(eq(id), any(TaskDto.class));
    }

    @Test
    void deleteTask_shouldDeleteTask() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(taskService).deleteTaskById(eq(id));

        mockMvc.perform(delete("/api/v1/tasks/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(taskService, Mockito.times(1)).deleteTaskById(eq(id));
    }
}