package database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import database.repositories.AppointmentRepository;
import database.repositories.ContactRepository;
import database.repositories.TaskRepository;

/**
 * Factory for creating and managing database repositories.
 * This class ensures that only one instance of each repository exists.
 */
public class RepositoryFactory {
    private static RepositoryFactory instance;
    private Map<Class<?>, DatabaseRepository<?>> repositories = new HashMap<>();
    
    // Private constructor for singleton pattern
    private RepositoryFactory() {
        // Initialize the database manager
        try {
            DatabaseManager.getInstance().initialize();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    /**
     * Get the singleton instance of the RepositoryFactory
     */
    public static synchronized RepositoryFactory getInstance() {
        if (instance == null) {
            instance = new RepositoryFactory();
        }
        return instance;
    }
    
    /**
     * Get a repository for the specified entity class
     */
    @SuppressWarnings("unchecked")
    public <T> DatabaseRepository<T> getRepository(Class<T> entityClass) throws SQLException {
        // Check if we already have a repository for this entity
        if (repositories.containsKey(entityClass)) {
            return (DatabaseRepository<T>) repositories.get(entityClass);
        }
        
        // Create a new repository based on the entity class
        DatabaseRepository<T> repository = createRepository(entityClass);
        
        // Initialize the repository (creates the table if needed)
        repository.initialize();
        
        // Cache the repository
        repositories.put(entityClass, repository);
        
        return repository;
    }
    
    /**
     * Create a repository for the specified entity class
     */
    @SuppressWarnings("unchecked")
    private <T> DatabaseRepository<T> createRepository(Class<T> entityClass) {
        String className = entityClass.getSimpleName();
        
        if (className.equals("Appointment")) {
            return (DatabaseRepository<T>) new AppointmentRepository();
        } else if (className.equals("Contact")) {
            return (DatabaseRepository<T>) new ContactRepository();
        } else if (className.equals("Task")) {
            return (DatabaseRepository<T>) new TaskRepository();
        } else {
            throw new IllegalArgumentException("No repository available for entity class: " + className);
        }
    }
    
    /**
     * Close all repositories and database connections
     */
    public void closeAll() throws SQLException {
        // Close all repositories
        for (DatabaseRepository<?> repository : repositories.values()) {
            repository.close();
        }
        
        // Clear the cache
        repositories.clear();
        
        // Close all database connections
        DatabaseManager.getInstance().closeAllConnections();
    }
}
