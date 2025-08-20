package database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import contact.Contact;
import database.BaseRepository;
import database.PersistableEntity;

/**
 * Repository for Contact entities
 */
public class ContactRepository extends BaseRepository<Contact> {
    
    /**
     * Constructor
     */
    public ContactRepository() {
        super("contacts", "contact_id");
    }
    
    /**
     * Get the SQL to create the contacts table
     */
    @Override
    protected String getCreateTableSql() {
        return "CREATE TABLE IF NOT EXISTS contacts (" +
               "contact_id VARCHAR(10) PRIMARY KEY, " +
               "first_name VARCHAR(10) NOT NULL, " +
               "last_name VARCHAR(10) NOT NULL, " +
               "phone VARCHAR(10) NOT NULL, " +
               "address VARCHAR(30) NOT NULL" +
               ")";
    }
    
    /**
     * Map a result set to a Contact entity
     */
    @Override
    protected Contact mapResultSetToEntity(ResultSet rs) throws SQLException {
        String contactId = rs.getString("contact_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        
        Contact contact = new Contact(contactId, firstName, lastName, phone, address);
        
        // Set the contact as persisted if applicable
        if (contact instanceof PersistableEntity) {
            ((PersistableEntity) contact).setPersisted(true);
        }
        
        return contact;
    }
    
    /**
     * Get the SQL for inserting a contact
     */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO contacts (contact_id, first_name, last_name, phone, address) " +
               "VALUES (?, ?, ?, ?, ?)";
    }
    
    /**
     * Set parameters for inserting a contact
     */
    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Contact contact) throws SQLException {
        pstmt.setString(1, contact.getContactId());
        pstmt.setString(2, contact.getFirstName());
        pstmt.setString(3, contact.getLastName());
        pstmt.setString(4, contact.getPhone());
        pstmt.setString(5, contact.getAddress());
    }
    
    /**
     * Get the SQL for updating a contact
     */
    @Override
    protected String getUpdateSql() {
        return "UPDATE contacts SET first_name = ?, last_name = ?, phone = ?, address = ? " +
               "WHERE contact_id = ?";
    }
    
    /**
     * Set parameters for updating a contact
     */
    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Contact contact) throws SQLException {
        pstmt.setString(1, contact.getFirstName());
        pstmt.setString(2, contact.getLastName());
        pstmt.setString(3, contact.getPhone());
        pstmt.setString(4, contact.getAddress());
        pstmt.setString(5, contact.getContactId());
    }
    
    /**
     * Get the ID from a contact
     */
    @Override
    protected String getEntityId(Contact contact) {
        return contact.getContactId();
    }
    
    /**
     * Find contacts by name (first name or last name)
     */
    public Contact findByName(String name) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE first_name = ? OR last_name = ? LIMIT 1";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
                return null;
            }
        }
    }
}
