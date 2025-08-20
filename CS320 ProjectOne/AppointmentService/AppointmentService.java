package appointment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.sql.SQLException;

/**
 * Service class for managing appointments with database integration
 */
public class AppointmentService {
    private Map<String, Appointment> appointmentCache = new HashMap<>();
    
    /**
     * Initializes the appointment service
     */
    public AppointmentService() {
        try {
            // Initialize database connection
            AppointmentDatabase.initialize();
            
            // Load existing appointments into cache
            loadAppointmentsFromDatabase();
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
    
    /**
     * Loads all appointments from the database into the cache
     */
    private void loadAppointmentsFromDatabase() throws SQLException {
        List<Appointment> appointments = AppointmentDatabase.getAllAppointments();
        appointmentCache.clear();
        
        for (Appointment appointment : appointments) {
            appointment.setPersisted(true);
            appointmentCache.put(appointment.getAppointmentId(), appointment);
        }
    }
    
    /**
     * Refreshes the cache from the database
     */
    public void refreshCache() throws SQLException {
        loadAppointmentsFromDatabase();
    }

    /**
     * Adds a new appointment
     */
    public void addAppointment(String appointmentId, Date appointmentDate, String description) throws SQLException {
        if(appointmentCache.containsKey(appointmentId) || AppointmentDatabase.appointmentExists(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }
        
        Appointment appointment = new Appointment(appointmentId, appointmentDate, description);
        
        // Save to database
        appointment.save();
        appointment.setPersisted(true);
        
        // Add to cache
        appointmentCache.put(appointmentId, appointment);
    }

    /**
     * Gets an appointment by ID (from cache)
     */
    public Appointment getAppointment(String appointmentId) {
        return appointmentCache.get(appointmentId);
    }

    /**
     * Gets an appointment by ID with robust error handling
     */
    public Appointment getAppointmentById(String appointmentId) throws SQLException {
        if(appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID cannot be null");
        }
        
        // Check cache first
        if(appointmentCache.containsKey(appointmentId)) {
            return appointmentCache.get(appointmentId);
        }
        
        // If not in cache, check database
        Appointment appointment = AppointmentDatabase.getAppointment(appointmentId);
        if(appointment == null) {
            throw new IllegalArgumentException("Appointment ID does not exist");
        }
        
        // Add to cache and return
        appointment.setPersisted(true);
        appointmentCache.put(appointmentId, appointment);
        return appointment;
    }

    /**
     * Deletes an appointment by ID
     */
    public void deleteAppointment(String appointmentId) throws SQLException {
        if(!appointmentCache.containsKey(appointmentId) && !AppointmentDatabase.appointmentExists(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID does not exist");
        }
        
        // Remove from database
        AppointmentDatabase.deleteAppointment(appointmentId);
        
        // Remove from cache
        appointmentCache.remove(appointmentId);
    }
    
    /**
     * Updates an appointment's details
     */
    public void updateAppointment(String appointmentId, Date appointmentDate, String description) throws SQLException {
        Appointment appointment = getAppointmentById(appointmentId);
        
        appointment.setAppointmentDate(appointmentDate);
        appointment.setDescription(description);
    }
    
    /**
     * Closes the database connection when service is no longer needed
     */
    public void close() throws SQLException {
        AppointmentDatabase.closeConnection();
    }
}
