package appointment;

import java.util.Date;
import java.sql.SQLException;
import database.PersistableEntity;

/**
 * Represents an appointment with database integration
 */
public class AppointmentWithDb extends Appointment implements PersistableEntity {
    private boolean isPersisted = false;
    
    /**
     * Creates a new appointment with the given details
     */
    public AppointmentWithDb(String appointmentId, Date appointmentDate, String description) {
        super(appointmentId, appointmentDate, description);
    }
    
    /**
     * Check if the appointment is persisted in the database
     */
    @Override
    public boolean isPersisted() {
        return isPersisted;
    }
    
    /**
     * Set the persisted state of the appointment
     */
    @Override
    public void setPersisted(boolean persisted) {
        this.isPersisted = persisted;
    }
}
