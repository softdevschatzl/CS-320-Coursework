package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for database operations.
 * This provides a common contract for all repository implementations.
 */
public interface DatabaseRepository<T> {
    
    /**
     * Initialize the repository
     */
    void initialize() throws SQLException;
    
    /**
     * Save an entity to the database
     */
    void save(T entity) throws SQLException;
    
    /**
     * Update an existing entity in the database
     */
    void update(T entity) throws SQLException;
    
    /**
     * Delete an entity from the database
     */
    void delete(String id) throws SQLException;
    
    /**
     * Find an entity by its ID
     */
    T findById(String id) throws SQLException;
    
    /**
     * Get all entities
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Check if an entity with the given ID exists
     */
    boolean exists(String id) throws SQLException;
    
    /**
     * Close the repository's resources
     */
    void close() throws SQLException;
}
