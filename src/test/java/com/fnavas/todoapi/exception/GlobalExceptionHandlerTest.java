package com.fnavas.todoapi.exception;

import com.fnavas.todoapi.dto.TaskFilter;
import com.fnavas.todoapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void illegalArgumentHandleException_shouldReturnBadRequest() throws Exception {
        Mockito.when(taskService.findAll(any(TaskFilter.class)))
                .thenThrow(new IllegalArgumentException("Invalid sortBy field: foo"));

        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Invalid argument"))
                .andExpect(jsonPath("$.error").value("Invalid sortBy field: foo"));
    }

    @Test
    void exceptionHandleException_shouldHandleException() throws Exception {
        Mockito.when(taskService.findAll(any(TaskFilter.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.error").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.message").value("Internal Server Error"));
    }
}