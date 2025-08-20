package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Global database manager for the application.
 * This class manages database connections and provides utility methods for database operations.
 */
public class DatabaseManager {
    // Singleton instance
    private static DatabaseManager instance;
    
    // Default database configuration
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:service_database.db";
    private static final String TEST_DB_URL_PREFIX = "jdbc:sqlite:";
    
    // Connection pool
    private Map<String, Connection> connections = new HashMap<>();
    
    // Current database URL
    private String currentDbUrl;
    
    // Private constructor for singleton pattern
    private DatabaseManager() {
        this.currentDbUrl = DEFAULT_DB_URL;
    }
    
    /**
     * Get the singleton instance of the DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize the database with a custom URL
     * @param dbUrl The database URL to use
     */
    public void initialize(String dbUrl) throws SQLException {
        this.currentDbUrl = dbUrl;
        getConnection();
    }
    
    /**
     * Initialize the database with the default URL
     */
    public void initialize() throws SQLException {
        initialize(DEFAULT_DB_URL);
    }
    
    /**
     * Create a test database with a unique name
     * @param testName A unique name for the test database
     * @return The URL of the created test database
     */
    public String createTestDatabase(String testName) throws SQLException {
        String testDbUrl = TEST_DB_URL_PREFIX + "test_" + testName + ".db";
        initialize(testDbUrl);
        return testDbUrl;
    }
    
    /**
     * Execute a SQL statement to create a table
     * @param sql The SQL statement to execute
     */
    public void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Get a connection to the database
     * @return A database connection
     */
    public synchronized Connection getConnection() throws SQLException {
        Connection connection = connections.get(currentDbUrl);
        
        if (connection == null || connection.isClosed()) {
            // Create a new connection
            connection = DriverManager.getConnection(currentDbUrl);
            connection.setAutoCommit(true);
            connections.put(currentDbUrl, connection);
        }
        
        return connection;
    }
    
    /**
     * Close all connections
     */
    public synchronized void closeAllConnections() throws SQLException {
        for (Connection connection : connections.values()) {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        connections.clear();
    }
    
    /**
     * Close a specific connection
     */
    public synchronized void closeConnection(String dbUrl) throws SQLException {
        Connection connection = connections.get(dbUrl);
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connections.remove(dbUrl);
        }
    }
    
    /**
     * Delete a test database
     */
    public void deleteTestDatabase(String testDbUrl) {
        try {
            closeConnection(testDbUrl);
            
            // Extract file path from JDBC URL
            String filePath = testDbUrl.replace(TEST_DB_URL_PREFIX, "");
            File dbFile = new File(filePath);
            
            if (dbFile.exists()) {
                dbFile.delete();
            }
        } catch (SQLException e) {
            // Log error but don't throw
            System.err.println("Error deleting test database: " + e.getMessage());
        }
    }
}
