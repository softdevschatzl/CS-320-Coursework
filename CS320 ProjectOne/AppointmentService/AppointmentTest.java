package test;

import appointment.Appointment;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentTest {

	@Test
    void testAppointmentConstructorValidData() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        Appointment appointment = new Appointment("A123456789", futureDate, "Description");
        assertEquals("A123456789", appointment.getAppointmentId());
        assertEquals(futureDate, appointment.getAppointmentDate());
        assertEquals("Description", appointment.getDescription());
    }

    @Test
    void testAppointmentConstructorInvalidAppointmentId() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        assertThrows(IllegalArgumentException.class, () -> new Appointment(null, futureDate, "Description"));
    }

    @Test
    void testAppointmentConstructorLongAppointmentId() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        assertThrows(IllegalArgumentException.class, () -> new Appointment("A1234567890", futureDate, "Description"));
    }

    @Test
    void testSetAppointmentDateInPast() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A123456789", futureDate, "Description");
        Date pastDate = new Date(System.currentTimeMillis() - 86400000);
        assertThrows(IllegalArgumentException.class, () -> appointment.setAppointmentDate(pastDate));
    }

    @Test
    void testSetDescriptionNull() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A123456789", futureDate, "Description");
        assertThrows(IllegalArgumentException.class, () -> appointment.setDescription(null));
    }

    @Test
    void testSetDescriptionTooLong() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A123456789", futureDate, "Description");
        String longDescription = "This description is definitely way too long to be considered valid for the appointment object";
        assertThrows(IllegalArgumentException.class, () -> appointment.setDescription(longDescription));
    }
}

