package task;
public class Task {
    private final String taskId;
    private String name;
    private String description;

    public Task(String taskId, String name, String description) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
    }

    public String getTaskId() {
        if (taskId == null || taskId.length() > 10) {
            throw new IllegalArgumentException("Invalid task id.");
        }
        return taskId;
    }

    public String getName() {
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException("Invalid task name.");
        }
        return name;
    }

    public String getDescription() {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid task description.");
        }
        return description;
    }
}