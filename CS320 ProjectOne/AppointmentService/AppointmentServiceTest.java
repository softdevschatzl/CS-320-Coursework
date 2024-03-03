package test;

import appointment.AppointmentService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;
import static org.junit.Assert.*;

public class AppointmentServiceTest {

	private AppointmentService appointmentService;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();
        futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
    }

    @Test
    void testAddAppointment() {
        appointmentService.addAppointment("A1", futureDate, "Checkup");
        assertNotNull(appointmentService.getAppointment("A1"));
    }

    @Test
    void testAddDuplicateAppointment() {
        appointmentService.addAppointment("A2", futureDate, "Meeting");
        assertThrows(IllegalArgumentException.class, () -> appointmentService.addAppointment("A2", futureDate, "Meeting"));
    }

    @Test
    void testDeleteAppointment() {
        appointmentService.addAppointment("A3", futureDate, "Interview");
        appointmentService.deleteAppointment("A3");
        assertNull(appointmentService.getAppointment("A3"));
    }

    @Test
    void testDeleteNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> appointmentService.deleteAppointment("A4"));
    }
}

