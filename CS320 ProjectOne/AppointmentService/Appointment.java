package appointment;

import java.util.Date;
import java.sql.SQLException;

/**
 * Represents an appointment with database integration
 */
public class Appointment {
	private final String appointmentId;
	private Date appointmentDate;
	private String description;
	private boolean isPersisted = false;
	
	/**
	 * Creates a new appointment with the given details
	 */
	public Appointment(String appointmentId, Date appointmentDate, String description) {
		if (appointmentId == null || appointmentId.length() > 10) {
			throw new IllegalArgumentException("Invalid appointment ID");
		}
		if (appointmentDate == null || appointmentDate.before(new Date())) {
			throw new IllegalArgumentException("Invalid appointment date");
		}
		if (description == null || description.length() > 50) {
			throw new IllegalArgumentException("Invalid description");
		}
		
		this.appointmentId = appointmentId;
		this.appointmentDate = appointmentDate;
		this.description = description;
	}
	
	/**
	 * Saves the appointment to the database
	 */
	public void save() throws SQLException {
		if (!isPersisted) {
			AppointmentDatabase.insertAppointment(this);
			isPersisted = true;
		} else {
			AppointmentDatabase.updateAppointment(this);
		}
	}
	
	/**
	 * Deletes this appointment from the database
	 */
	public void delete() throws SQLException {
		AppointmentDatabase.deleteAppointment(this.appointmentId);
		isPersisted = false;
	}
	
	/**
	 * Loads an appointment from the database by ID
	 */
	public static Appointment load(String appointmentId) throws SQLException {
		return AppointmentDatabase.getAppointment(appointmentId);
	}
	
	// Getters
	public String getAppointmentId() {
		return appointmentId;
	}
	
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isPersisted() {
		return isPersisted;
	}
	
	// Setters
	public void setAppointmentDate(Date appointmentDate) throws SQLException {
        if(appointmentDate == null || appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Invalid appointment date");
        }
        this.appointmentDate = appointmentDate;
        
        // Update in database if persisted
        if (isPersisted) {
            AppointmentDatabase.updateAppointment(this);
        }
    }

    public void setDescription(String description) throws SQLException {
        if(description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid description");
        }
        this.description = description;
        
        // Update in database if persisted
        if (isPersisted) {
            AppointmentDatabase.updateAppointment(this);
        }
    }
    
    /**
     * Sets the persisted flag (used internally)
     */
    void setPersisted(boolean persisted) {
        this.isPersisted = persisted;
    }
}