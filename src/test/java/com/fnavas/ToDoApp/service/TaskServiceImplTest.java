package com.fnavas.ToDoApp.service;

import com.fnavas.ToDoApp.dto.TaskDto;
import com.fnavas.ToDoApp.entity.Task;
import com.fnavas.ToDoApp.exception.TaskNotFoundException;
import com.fnavas.ToDoApp.mapper.TaskMapper;
import com.fnavas.ToDoApp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        TaskDto sampleTaskDto = new TaskDto();
        sampleTaskDto.setId(1L);
        sampleTaskDto.setTitle("title");
        sampleTaskDto.setDescription("description");
        return sampleTaskDto;
    }

    @Test
    public void getTaskById_returnsTask() {
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findById(1L)).thenReturn(Optional.of(mockTask));
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        TaskDto taskDto = taskServiceImpl.findById(1L);

        assertNotNull(taskDto);
        assertEquals(taskDto.getTitle(), mockTask.getTitle());
        assertEquals(taskDto.getDescription(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getTaskById_returnsTaskNotFoundException() {
        Long id = 99L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImpl.findById(id));

        assertEquals("Task with id " + id + " not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void findByCompleted_returnsTasks() {
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());

        Mockito.when(taskRepository.findByCompleted(true)).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findByCompleted(true);

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findByCompleted(true);
    }

    @Test
    public void findAll_returnsTasks() {
        List<Task> mockTasks = List.of(sampleTask(), sampleTask());
        Task  mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskRepository.findAll()).thenReturn(mockTasks);
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);

        List<TaskDto> taskDtos = taskServiceImpl.findAll();

        assertNotNull(taskDtos);
        assertEquals(taskDtos.size(), mockTasks.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void createTask() {
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        Mockito.when(taskMapper.toEntity(mockTaskDto)).thenReturn(mockTask);
        Mockito.when(taskRepository.save(mockTask)).thenReturn(mockTask);

        TaskDto taskDto = taskServiceImpl.createTask(mockTaskDto);

        assertNotNull(taskDto);
        assertEquals(taskDto.getTitle(), mockTask.getTitle());
        assertEquals(taskDto.getDescription(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).save(mockTask);

    }

    @Test
    public void updateTaskById_whenTaskExits_returnsTask() {
        Long id = 1L;
        Task mockTask = sampleTask();
        TaskDto mockTaskDto = sampleTaskDto();
        Mockito.when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        Mockito.when(taskRepository.save(mockTask)).thenReturn(mockTask);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));

        TaskDto taskDto = taskServiceImpl.updateTaskById(id, mockTaskDto);

        assertNotNull(taskDto);
        assertEquals(taskDto.getTitle(), mockTask.getTitle());
        assertEquals(taskDto.getDescription(), mockTask.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).save(mockTask);
    }

    @Test
    public void updateTask_whenTaskDoesNotExits_returnsTaskNotFoundException() {
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
    public void deleteTaskById_whenTaskExits_shouldDeleteTask() {
        Long id = 1L;
        Task mockTask = sampleTask();
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));

        taskServiceImpl.deleteTaskById(id);

        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(id);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void deleteTask_whenTaskDoesNotExits_shouldDeleteTaskNotFoundException() {
        Long id = 99L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskServiceImpl.deleteTaskById(id));

        assertEquals("Task with id " + id + " not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
    }
}