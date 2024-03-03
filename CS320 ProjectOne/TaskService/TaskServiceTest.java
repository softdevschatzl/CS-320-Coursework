package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import task.TaskService;

public class TaskServiceTest {
	
	private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService();
    }

    @Test
    void testAddTask() {
        taskService.addTask("T1", "Task 1", "Description 1");
        assertTrue(taskService.taskExists("T1"));
        assertEquals("Task 1", taskService.getTaskName("T1"));
        assertEquals("Description 1", taskService.getTaskDescription("T1"));
    }

    @Test
    void testDeleteTask() {
        taskService.addTask("T2", "Task 2", "Description 2");
        taskService.deleteTask("T2");
        assertFalse(taskService.taskExists("T2"));
    }

    @Test
    void testUpdateTaskName() {
        taskService.addTask("T3", "Task 3", "Description 3");
        taskService.updateTaskName("T3", "Updated Task 3");
        assertEquals("Updated Task 3", taskService.getTaskName("T3"));
    }

    @Test
    void testUpdateTaskDescription() {
        taskService.addTask("T4", "Task 4", "Description 4");
        taskService.updateTaskDescription("T4", "Updated Description 4");
        assertEquals("Updated Description 4", taskService.getTaskDescription("T4"));
    }

    @Test
    void testUpdateTaskNameWithNull() {
        taskService.addTask("T5", "Task 5", "Description 5");
        taskService.updateTaskName("T5", null);
        assertEquals("Task 5", taskService.getTaskName("T5")); // Name should remain unchanged
    }

    @Test
    void testUpdateTaskDescriptionWithNull() {
        taskService.addTask("T6", "Task 6", "Description 6");
        taskService.updateTaskDescription("T6", null);
        assertEquals("Description 6", taskService.getTaskDescription("T6")); // Description should remain unchanged
    }
}
