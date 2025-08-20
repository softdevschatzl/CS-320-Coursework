package database.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.BaseRepository;
import database.PersistableEntity;
import task.Task;

/**
 * Repository for Task entities
 */
public class TaskRepository extends BaseRepository<Task> {
    
    /**
     * Constructor
     */
    public TaskRepository() {
        super("tasks", "task_id");
    }
    
    /**
     * Get the SQL to create the tasks table
     */
    @Override
    protected String getCreateTableSql() {
        return "CREATE TABLE IF NOT EXISTS tasks (" +
               "task_id VARCHAR(10) PRIMARY KEY, " +
               "name VARCHAR(20) NOT NULL, " +
               "description VARCHAR(50) NOT NULL" +
               ")";
    }
    
    /**
     * Map a result set to a Task entity
     */
    @Override
    protected Task mapResultSetToEntity(ResultSet rs) throws SQLException {
        String taskId = rs.getString("task_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        
        Task task = new Task(taskId, name, description);
        
        // Set the task as persisted if applicable
        if (task instanceof PersistableEntity) {
            ((PersistableEntity) task).setPersisted(true);
        }
        
        return task;
    }
    
    /**
     * Get the SQL for inserting a task
     */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO tasks (task_id, name, description) VALUES (?, ?, ?)";
    }
    
    /**
     * Set parameters for inserting a task
     */
    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Task task) throws SQLException {
        pstmt.setString(1, task.getTaskId());
        pstmt.setString(2, task.getName());
        pstmt.setString(3, task.getDescription());
    }
    
    /**
     * Get the SQL for updating a task
     */
    @Override
    protected String getUpdateSql() {
        return "UPDATE tasks SET name = ?, description = ? WHERE task_id = ?";
    }
    
    /**
     * Set parameters for updating a task
     */
    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Task task) throws SQLException {
        pstmt.setString(1, task.getName());
        pstmt.setString(2, task.getDescription());
        pstmt.setString(3, task.getTaskId());
    }
    
    /**
     * Get the ID from a task
     */
    @Override
    protected String getEntityId(Task task) {
        return task.getTaskId();
    }
}
