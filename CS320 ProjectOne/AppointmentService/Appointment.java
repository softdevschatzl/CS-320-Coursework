package appointment;

import java.util.Date;

public class Appointment {
	private final String appointmentId;
	private Date appointmentDate;
	private String description;
	
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
	
	// Setters
	public void setAppointmentDate(Date appointmentDate) {
        if(appointmentDate == null || appointmentDate.before(new Date())) {
            throw new IllegalArgumentException("Invalid appointment date");
        }
        this.appointmentDate = appointmentDate;
    }

    public void setDescription(String description) {
        if(description == null || description.length() > 50) {
            throw new IllegalArgumentException("Invalid description");
        }
        this.description = description;
    }
}