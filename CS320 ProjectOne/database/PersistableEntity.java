package database;

/**
 * Interface for entities that can be persisted in the database.
 * This interface should be implemented by entity classes that need to track their persistence state.
 */
public interface PersistableEntity {
    
    /**
     * Check if the entity is persisted in the database
     * 
     * @return true if the entity is persisted, false otherwise
     */
    boolean isPersisted();
    
    /**
     * Set the persisted state of the entity
     * 
     * @param persisted The persisted state
     */
    void setPersisted(boolean persisted);
}
