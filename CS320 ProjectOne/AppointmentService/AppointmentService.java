package appointment;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class AppointmentService {
    private Map<String, Appointment> appointments = new HashMap<>();

    public void addAppointment(String appointmentId, Date appointmentDate, String description) {
        if(appointments.containsKey(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }
        appointments.put(appointmentId, new Appointment(appointmentId, appointmentDate, description));
    }

    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }

    public void deleteAppointment(String appointmentId) {
        if(!appointments.containsKey(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID does not exist");
        }
        appointments.remove(appointmentId);
    }
}
