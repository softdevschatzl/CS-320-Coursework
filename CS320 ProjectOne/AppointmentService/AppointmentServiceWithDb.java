package appointment;

import java.util.Date;
import java.sql.SQLException;
import java.util.List;

import database.ServiceHelper;

/**
 * Enhanced AppointmentService with database integration using the global database framework
 */
public class AppointmentServiceWithDb {
    private final ServiceHelper<Appointment> serviceHelper;
    
    /**
     * Initialize the appointment service with database support
     */
    public AppointmentServiceWithDb() {
        this.serviceHelper = new ServiceHelper<>(Appointment.class);
    }
    
    /**
     * Add a new appointment
     */
    public void addAppointment(String appointmentId, Date appointmentDate, String description) throws SQLException {
        // Check if appointment already exists
        if (serviceHelper.exists(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }
        
        // Create new appointment
        Appointment appointment = new AppointmentWithDb(appointmentId, appointmentDate, description);
        
        // Save to database
        serviceHelper.save(appointment);
    }
    
    /**
     * Get an appointment by ID
     */
    public Appointment getAppointment(String appointmentId) throws SQLException {
        return serviceHelper.getById(appointmentId);
    }
    
    /**
     * Get an appointment by ID with robust error handling
     */
    public Appointment getAppointmentById(String appointmentId) throws SQLException {
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        
        Appointment appointment = serviceHelper.getById(appointmentId);
        
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment ID does not exist");
        }
        
        return appointment;
    }
    
    /**
     * Delete an appointment by ID
     */
    public void deleteAppointment(String appointmentId) throws SQLException {
        if (!serviceHelper.exists(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID does not exist");
        }
        
        serviceHelper.delete(appointmentId);
    }
    
    /**
     * Update an appointment's details
     */
    public void updateAppointment(String appointmentId, Date appointmentDate, String description) throws SQLException {
        // Get the existing appointment
        Appointment appointment = getAppointmentById(appointmentId);
        
        // Update the appointment
        appointment.setAppointmentDate(appointmentDate);
        appointment.setDescription(description);
        
        // Save changes to database
        serviceHelper.update(appointment);
    }
    
    /**
     * Get all appointments
     */
    public List<Appointment> getAllAppointments() throws SQLException {
        return serviceHelper.getAll();
    }
    
    /**
     * Refresh the cache from the database
     */
    public void refreshCache() throws SQLException {
        serviceHelper.refreshCache();
    }
    
    /**
     * Close the service
     */
    public void close() throws SQLException {
        serviceHelper.close();
    }
}
