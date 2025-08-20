package database.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import appointment.Appointment;
import database.BaseRepository;

/**
 * Repository for Appointment entities
 */
public class AppointmentRepository extends BaseRepository<Appointment> {
    
    /**
     * Constructor
     */
    public AppointmentRepository() {
        super("appointments", "appointment_id");
    }
    
    /**
     * Get the SQL to create the appointments table
     */
    @Override
    protected String getCreateTableSql() {
        return "CREATE TABLE IF NOT EXISTS appointments (" +
               "appointment_id VARCHAR(10) PRIMARY KEY, " +
               "appointment_date TIMESTAMP NOT NULL, " +
               "description VARCHAR(50) NOT NULL" +
               ")";
    }
    
    /**
     * Map a result set to an Appointment entity
     */
    @Override
    protected Appointment mapResultSetToEntity(ResultSet rs) throws SQLException {
        String appointmentId = rs.getString("appointment_id");
        Date appointmentDate = new Date(rs.getTimestamp("appointment_date").getTime());
        String description = rs.getString("description");
        
        Appointment appointment = new Appointment(appointmentId, appointmentDate, description);
        
        // Set the appointment as persisted if applicable
        if (appointment instanceof PersistableEntity) {
            ((PersistableEntity) appointment).setPersisted(true);
        }
        
        return appointment;
    }
    
    /**
     * Get the SQL for inserting an appointment
     */
    @Override
    protected String getInsertSql() {
        return "INSERT INTO appointments (appointment_id, appointment_date, description) " +
               "VALUES (?, ?, ?)";
    }
    
    /**
     * Set parameters for inserting an appointment
     */
    @Override
    protected void setInsertParameters(PreparedStatement pstmt, Appointment appointment) throws SQLException {
        pstmt.setString(1, appointment.getAppointmentId());
        pstmt.setTimestamp(2, new Timestamp(appointment.getAppointmentDate().getTime()));
        pstmt.setString(3, appointment.getDescription());
    }
    
    /**
     * Get the SQL for updating an appointment
     */
    @Override
    protected String getUpdateSql() {
        return "UPDATE appointments SET appointment_date = ?, description = ? " +
               "WHERE appointment_id = ?";
    }
    
    /**
     * Set parameters for updating an appointment
     */
    @Override
    protected void setUpdateParameters(PreparedStatement pstmt, Appointment appointment) throws SQLException {
        pstmt.setTimestamp(1, new Timestamp(appointment.getAppointmentDate().getTime()));
        pstmt.setString(2, appointment.getDescription());
        pstmt.setString(3, appointment.getAppointmentId());
    }
    
    /**
     * Get the ID from an appointment
     */
    @Override
    protected String getEntityId(Appointment appointment) {
        return appointment.getAppointmentId();
    }
}
