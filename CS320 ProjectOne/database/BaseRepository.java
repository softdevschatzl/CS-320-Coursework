package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base repository implementing common database operations.
 * This class provides a template for concrete repository implementations.
 * 
 * @param <T> The entity type this repository manages
 */
public abstract class BaseRepository<T> implements DatabaseRepository<T> {
    
    protected final DatabaseManager dbManager;
    protected final String tableName;
    protected final String idColumnName;
    
    /**
     * Constructor for the base repository
     * 
     * @param tableName The name of the database table
     * @param idColumnName The name of the ID column in the table
     */
    public BaseRepository(String tableName, String idColumnName) {
        this.dbManager = DatabaseManager.getInstance();
        this.tableName = tableName;
        this.idColumnName = idColumnName;
    }
    
    /**
     * Initialize the repository by creating the necessary table
     */
    @Override
    public void initialize() throws SQLException {
        String createTableSql = getCreateTableSql();
        dbManager.executeUpdate(createTableSql);
    }
    
    /**
     * Get the SQL statement to create the table
     */
    protected abstract String getCreateTableSql();
    
    /**
     * Map a database result set to an entity
     */
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;
    
    /**
     * Get the SQL statement to insert an entity
     */
    protected abstract String getInsertSql();
    
    /**
     * Set parameters for the insert statement
     */
    protected abstract void setInsertParameters(PreparedStatement pstmt, T entity) throws SQLException;
    
    /**
     * Get the SQL statement to update an entity
     */
    protected abstract String getUpdateSql();
    
    /**
     * Set parameters for the update statement
     */
    protected abstract void setUpdateParameters(PreparedStatement pstmt, T entity) throws SQLException;
    
    /**
     * Get the ID from an entity
     */
    protected abstract String getEntityId(T entity);
    
    /**
     * Save an entity to the database
     */
    @Override
    public void save(T entity) throws SQLException {
        String id = getEntityId(entity);
        if (exists(id)) {
            update(entity);
        } else {
            try (Connection conn = dbManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(getInsertSql())) {
                
                setInsertParameters(pstmt, entity);
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Update an existing entity in the database
     */
    @Override
    public void update(T entity) throws SQLException {
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(getUpdateSql())) {
            
            setUpdateParameters(pstmt, entity);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Delete an entity from the database
     */
    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Find an entity by its ID
     */
    @Override
    public T findById(String id) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
                return null;
            }
        }
    }
    
    /**
     * Get all entities
     */
    @Override
    public List<T> findAll() throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        }
        
        return entities;
    }
    
    /**
     * Check if an entity with the given ID exists
     */
    @Override
    public boolean exists(String id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idColumnName + " = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
    
    /**
     * Close the repository's resources
     */
    @Override
    public void close() throws SQLException {
        // No need to close anything here as the connection is managed by DatabaseManager
    }
}
