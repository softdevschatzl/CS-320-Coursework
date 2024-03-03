package task;
import java.util.HashMap;
import java.util.Map;

public class TaskService {
    private Map<String, Task> tasks;

    public TaskService() {
        tasks = new HashMap<>();
    }

    public void addTask(String taskId, String name, String description) {
        Task task = new Task(name, description);
        tasks.put(taskId, task);
    }

    public void deleteTask(String taskId) {
        tasks.remove(taskId);
    }

    public void updateTaskName(String taskId, String newName) {
        Task task = tasks.get(taskId);
        if (task != null && newName != null && newName.length() <= 20) {
            task.setName(newName);
        }
    }

    public void updateTaskDescription(String taskId, String newDescription) {
        Task task = tasks.get(taskId);
        if (task != null && newDescription != null && newDescription.length() <= 50) {
            task.setDescription(newDescription);
        }
    }
    
    // Test methods
    
    public boolean taskExists(String taskId) {
    	return tasks.containsKey(taskId);
    }
    
    public String getTaskName(String taskId) {
        Task task = tasks.get(taskId);
        return (task != null) ? task.getName() : null;    	
    }
    
    public String getTaskDescription(String taskId) {
        Task task = tasks.get(taskId);
        return (task != null) ? task.getDescription() : null;
    }

    // Private class and methods

    private class Task {
        private String name;
        private String description;

        public Task(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
