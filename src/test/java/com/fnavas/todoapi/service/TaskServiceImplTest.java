package com.fnavas.todoapi.service;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.dto.TaskFilter;
import com.fnavas.todoapi.entity.Task;
import com.fnavas.todoapi.exception.TaskNotFoundException;
import com.fnavas.todoapi.mapper.TaskMapper;
import com.fnavas.todoapi.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private Task sampleTask() {
        Task sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("title");
        sampleTask.setDescription("description");
        return sampleTask;
    }

    private TaskDto sampleTaskDto() {
        return new TaskDto(1L, "title", "description", null, null);
    }

    @Test
    void getTaskById_returnsTask() {
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        TaskDto taskDto = taskServiceImpl.findById(1L);

        assertNotNull(taskDto);
        assertEquals(taskDto.title(), mockTask.getTitle());
        assertEquals(taskDto.description(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getTaskById_returnsTaskNotFoundException() {
        Long id = 99L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImpl.findById(id));

        assertEquals("Task with id " + id + " not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void findAll_returnsTasks() {
        TaskFilter taskFilter = new TaskFilter();
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll(taskFilter);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Specification.class));
    }

    @Test
    void findAll_withTitle_returnsTasks() {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTitle("title");
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll(taskFilter);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Specification.class));
    }

    @Test
    void findAll_withBlankTitleAndDescription_returnsTasks() {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTitle(" ");
        taskFilter.setDescription(" ");
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll(taskFilter);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Specification.class));
    }

    @Test
    void findAll_withDescription_returnsTasks() {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setDescription("description");
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll(taskFilter);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Specification.class));
    }

    @Test
    void findAll_withTitleAndDescription_returnsTasks() {
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTitle("title");
        taskFilter.setDescription("description");
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll(any(Specification.class))).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll(taskFilter);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Specification.class));
    }

    @Test
    void createTask() {
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        Mockito.when(taskMapper.toEntity(mockTaskDto)).thenReturn(mockTask);
        Mockito.when(taskRepository.save(mockTask)).thenReturn(mockTask);

        TaskDto taskDto = taskServiceImpl.createTask(mockTaskDto);

        assertNotNull(taskDto);
        assertEquals(taskDto.title(), mockTask.getTitle());
        assertEquals(taskDto.description(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).save(mockTask);
    }

    @Test
    void updateTaskById_whenTaskExits_returnsTask() {
        Long id = 1L;
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        Mockito.when(taskRepository.save(mockTask)).thenReturn(mockTask);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));

        TaskDto taskDto = taskServiceImpl.updateTaskById(id, mockTaskDto);

        assertNotNull(taskDto);
        assertEquals(taskDto.title(), mockTask.getTitle());
        assertEquals(taskDto.description(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).save(mockTask);
    }

    @Test
    void updateTask_whenTaskDoesNotExits_returnsTaskNotFoundException() {
        Long id = 99L;
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImpl.updateTaskById(id, mockTaskDto)
        );

        assertEquals("Task with id " + id + " not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);

    }

    @Test
    void deleteTaskById_whenTaskExits_shouldDeleteTask() {
        Long id = 1L;
        Task mockTask = sampleTask();
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));

        taskServiceImpl.deleteTaskById(id);

        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(id);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deleteTask_whenTaskDoesNotExits_shouldDeleteTaskNotFoundException() {
        Long id = 99L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImpl.deleteTaskById(id));

        assertEquals("Task with id " + id + " not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }
}