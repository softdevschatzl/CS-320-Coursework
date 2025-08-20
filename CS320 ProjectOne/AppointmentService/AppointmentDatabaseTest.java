package test;

import appointment.Appointment;
import appointment.AppointmentDatabase;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentDatabaseTest {
    
    @TempDir
    static Path tempDir;
    private static String dbPath;
    private static Connection connection;
    
    @BeforeAll
    static void setupDatabase() throws SQLException {
        // Create a temporary database file
        dbPath = tempDir.resolve("test_appointments.db").toString();
        
        // Set the DB_URL field in AppointmentDatabase through reflection
        try {
            java.lang.reflect.Field field = AppointmentDatabase.class.getDeclaredField("DB_URL");
            field.setAccessible(true);
            field.set(null, "jdbc:sqlite:" + dbPath);
        } catch (Exception e) {
            fail("Could not set test database URL: " + e.getMessage());
        }
        
        // Initialize the database
        AppointmentDatabase.initialize();
    }
    
    @AfterAll
    static void cleanupDatabase() throws SQLException {
        AppointmentDatabase.closeConnection();
        
        // Delete the temporary database file
        new File(dbPath).delete();
    }
    
    @Test
    void testInsertAndRetrieveAppointment() throws SQLException {
        // Create test appointment
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        Appointment appointment = new Appointment("DB1", futureDate, "Database Test");
        
        // Insert into database
        AppointmentDatabase.insertAppointment(appointment);
        
        // Retrieve and verify
        Appointment retrievedAppointment = AppointmentDatabase.getAppointment("DB1");
        assertNotNull(retrievedAppointment);
        assertEquals("DB1", retrievedAppointment.getAppointmentId());
        assertEquals("Database Test", retrievedAppointment.getDescription());
    }
    
    @Test
    void testUpdateAppointment() throws SQLException {
        // Create and insert test appointment
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("DB2", futureDate, "Original Description");
        AppointmentDatabase.insertAppointment(appointment);
        
        // Update description
        appointment.setDescription("Updated Description");
        AppointmentDatabase.updateAppointment(appointment);
        
        // Retrieve and verify
        Appointment retrievedAppointment = AppointmentDatabase.getAppointment("DB2");
        assertEquals("Updated Description", retrievedAppointment.getDescription());
    }
    
    @Test
    void testDeleteAppointment() throws SQLException {
        // Create and insert test appointment
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("DB3", futureDate, "To Be Deleted");
        AppointmentDatabase.insertAppointment(appointment);
        
        // Verify it exists
        assertTrue(AppointmentDatabase.appointmentExists("DB3"));
        
        // Delete it
        AppointmentDatabase.deleteAppointment("DB3");
        
        // Verify it's gone
        assertFalse(AppointmentDatabase.appointmentExists("DB3"));
        assertNull(AppointmentDatabase.getAppointment("DB3"));
    }
    
    @Test
    void testGetAllAppointments() throws SQLException {
        // Clear any existing appointments
        for (Appointment appt : AppointmentDatabase.getAllAppointments()) {
            AppointmentDatabase.deleteAppointment(appt.getAppointmentId());
        }
        
        // Create and insert test appointments
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        AppointmentDatabase.insertAppointment(new Appointment("DBA", futureDate, "First Appointment"));
        AppointmentDatabase.insertAppointment(new Appointment("DBB", futureDate, "Second Appointment"));
        AppointmentDatabase.insertAppointment(new Appointment("DBC", futureDate, "Third Appointment"));
        
        // Get all and verify
        assertEquals(3, AppointmentDatabase.getAllAppointments().size());
    }
}
