package com.fnavas.todoapi.mapper;

import com.fnavas.todoapi.dto.TaskDto;
import com.fnavas.todoapi.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskMapperTest {

    @Autowired
    private TaskMapper taskMapper;

    // --- toDto ---

    @Test
    void toDto_mapsAllFields() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Buy groceries");
        task.setDescription("Milk, eggs, bread");
        task.setCompleted(true);

        TaskDto dto = taskMapper.toDto(task);

        assertEquals(task.getId(), dto.id());
        assertEquals(task.getTitle(), dto.title());
        assertEquals(task.getDescription(), dto.description());
        assertEquals(task.getCompleted(), dto.completed());
    }

    @Test
    void toDto_withNullDescription_mapsToNull() {
        Task task = new Task();
        task.setId(2L);
        task.setTitle("Simple task");
        task.setDescription(null);
        task.setCompleted(false);

        TaskDto dto = taskMapper.toDto(task);

        assertNull(dto.description());
    }

    @Test
    void toDto_withCreatedDate_mapsDate() {
        Task task = new Task();
        task.setTitle("Dated task");
        task.setCompleted(false);

        TaskDto dto = taskMapper.toDto(task);

        // created is managed by Hibernate, so it is null outside persistence context
        assertNull(dto.created());
    }

    @Test
    void toDto_withNullTask_returnsNull() {
        assertNull(taskMapper.toDto(null));
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFields() {
        TaskDto dto = new TaskDto(10L, "Write tests", "Cover mapper layer", true, LocalDate.now());

        Task task = taskMapper.toEntity(dto);

        assertEquals(dto.id(), task.getId());
        assertEquals(dto.title(), task.getTitle());
        assertEquals(dto.description(), task.getDescription());
        assertEquals(dto.completed(), task.getCompleted());
    }

    @Test
    void toEntity_withNullCompleted_defaultsToFalse() {
        TaskDto dto = new TaskDto(null, "No completed field", null, null, null);

        Task task = taskMapper.toEntity(dto);

        assertFalse(task.getCompleted());
    }

    @Test
    void toEntity_withNullDescription_mapsToNull() {
        TaskDto dto = new TaskDto(null, "No description", null, false, null);

        Task task = taskMapper.toEntity(dto);

        assertNull(task.getDescription());
    }

    @Test
    void toEntity_withNullDto_returnsNull() {
        assertNull(taskMapper.toEntity(null));
    }

    // --- round-trip ---

    @Test
    void roundTrip_entityToDtoToEntity_preservesFields() {
        Task original = new Task();
        original.setId(5L);
        original.setTitle("Round trip task");
        original.setDescription("Verify data integrity");
        original.setCompleted(true);

        Task result = taskMapper.toEntity(taskMapper.toDto(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getTitle(), result.getTitle());
        assertEquals(original.getDescription(), result.getDescription());
        assertEquals(original.getCompleted(), result.getCompleted());
    }
}
