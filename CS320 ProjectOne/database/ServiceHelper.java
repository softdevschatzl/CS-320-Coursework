package database;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A generic service helper that provides database operations for any entity type.
 * This class acts as a bridge between service classes and repositories.
 * 
 * @param <T> The entity type this service helper manages
 */
public class ServiceHelper<T> {
    
    private final Class<T> entityClass;
    private final Map<String, T> cache = new ConcurrentHashMap<>();
    private DatabaseRepository<T> repository;
    
    /**
     * Constructor
     * 
     * @param entityClass The class of the entity this helper manages
     */
    public ServiceHelper(Class<T> entityClass) {
        this.entityClass = entityClass;
        
        try {
            // Get the repository for this entity type
            this.repository = RepositoryFactory.getInstance().getRepository(entityClass);
            
            // Load all entities into the cache
            refreshCache();
        } catch (SQLException e) {
            System.err.println("Error initializing service helper: " + e.getMessage());
        }
    }
    
    /**
     * Refresh the cache from the database
     */
    public void refreshCache() throws SQLException {
        // Clear the cache
        cache.clear();
        
        // Load all entities from the database
        List<T> entities = repository.findAll();
        
        // Add all entities to the cache
        for (T entity : entities) {
            String id = getEntityId(entity);
            cache.put(id, entity);
        }
    }
    
    /**
     * Save an entity to the database and cache
     */
    public void save(T entity) throws SQLException {
        // Save to database
        repository.save(entity);
        
        // Add to cache
        String id = getEntityId(entity);
        cache.put(id, entity);
        
        // Set as persisted if applicable
        if (entity instanceof PersistableEntity) {
            ((PersistableEntity) entity).setPersisted(true);
        }
    }
    
    /**
     * Update an existing entity in the database and cache
     */
    public void update(T entity) throws SQLException {
        // Update in database
        repository.update(entity);
        
        // Update in cache
        String id = getEntityId(entity);
        cache.put(id, entity);
    }
    
    /**
     * Delete an entity from the database and cache
     */
    public void delete(String id) throws SQLException {
        // Delete from database
        repository.delete(id);
        
        // Remove from cache
        cache.remove(id);
    }
    
    /**
     * Get an entity by ID from the cache or database
     */
    public T getById(String id) throws SQLException {
        // Check cache first
        T entity = cache.get(id);
        
        // If not in cache, check database
        if (entity == null) {
            entity = repository.findById(id);
            
            // Add to cache if found
            if (entity != null) {
                cache.put(id, entity);
                
                // Set as persisted if applicable
                if (entity instanceof PersistableEntity) {
                    ((PersistableEntity) entity).setPersisted(true);
                }
            }
        }
        
        return entity;
    }
    
    /**
     * Check if an entity with the given ID exists
     */
    public boolean exists(String id) throws SQLException {
        return cache.containsKey(id) || repository.exists(id);
    }
    
    /**
     * Get all entities from the cache
     */
    public List<T> getAll() throws SQLException {
        // Ensure cache is up to date
        refreshCache();
        
        // Return all entities in the cache
        return List.copyOf(cache.values());
    }
    
    /**
     * Close the service helper's resources
     */
    public void close() throws SQLException {
        cache.clear();
        repository.close();
    }
    
    /**
     * Get the ID from an entity
     */
    @SuppressWarnings("unchecked")
    private String getEntityId(T entity) {
        // Use reflection to call the appropriate getter method
        try {
            if (entityClass.getSimpleName().equals("Appointment")) {
                return (String) entityClass.getMethod("getAppointmentId").invoke(entity);
            } else if (entityClass.getSimpleName().equals("Contact")) {
                return (String) entityClass.getMethod("getContactId").invoke(entity);
            } else if (entityClass.getSimpleName().equals("Task")) {
                return (String) entityClass.getMethod("getTaskId").invoke(entity);
            } else {
                throw new IllegalArgumentException("Unknown entity type: " + entityClass.getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting entity ID: " + e.getMessage(), e);
        }
    }
}
