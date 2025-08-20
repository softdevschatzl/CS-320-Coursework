package test;
import org.junit.Test;

import task.Task;

import static org.junit.Assert.*;
public class TaskTest {

	@Test
    void testTaskConstructorValidData() {
        Task task = new Task("T123456789", "Task Name", "Task Description");
        assertEquals("T123456789", task.getTaskId());
        assertEquals("Task Name", task.getName());
        assertEquals("Task Description", task.getDescription());
    }

    @Test
    void testTaskConstructorInvalidTaskId() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null, "Task Name", "Task Description"));
    }

    @Test
    void testTaskConstructorLongTaskId() {
        assertThrows(IllegalArgumentException.class, () -> new Task("T1234567890", "Task Name", "Task Description"));
    }

    @Test
    void testGetNameInvalid() {
        Task task = new Task("T123456789", null, "Task Description");
        assertThrows(IllegalArgumentException.class, task::getName);
    }

    @Test
    void testGetNameTooLong() {
        Task task = new Task("T123456789", "This name is definitely way too long to be valid", "Task Description");
        assertThrows(IllegalArgumentException.class, task::getName);
    }

    @Test
    void testGetDescriptionInvalid() {
        Task task = new Task("T123456789", "Task Name", null);
        assertThrows(IllegalArgumentException.class, task::getDescription);
    }

    @Test
    void testGetDescriptionTooLong() {
        Task task = new Task("T123456789", "Task Name", "This description is definitely way too long to be considered valid for the task object");
        assertThrows(IllegalArgumentException.class, task::getDescription);
    }
}
