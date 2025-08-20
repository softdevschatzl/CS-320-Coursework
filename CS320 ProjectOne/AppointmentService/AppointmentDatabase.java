package appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Database helper class for Appointment related operations
 */
public class AppointmentDatabase {
    // Database configuration
    private static final String DB_URL = "jdbc:sqlite:appointments.db";
    private static Connection connection;

    /**
     * Initializes the database connection and creates tables if they don't exist
     */
    public static void initialize() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        createTables();
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Creates the necessary tables in the database
     */
    private static void createTables() throws SQLException {
        String createTableSQL = 
            "CREATE TABLE IF NOT EXISTS appointments (" +
            "appointment_id VARCHAR(10) PRIMARY KEY, " +
            "appointment_date TIMESTAMP NOT NULL, " +
            "description VARCHAR(50) NOT NULL" +
            ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    /**
     * Inserts a new appointment into the database
     */
    public static void insertAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (appointment_id, appointment_date, description) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, appointment.getAppointmentId());
            pstmt.setTimestamp(2, new Timestamp(appointment.getAppointmentDate().getTime()));
            pstmt.setString(3, appointment.getDescription());
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates an existing appointment in the database
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET appointment_date = ?, description = ? WHERE appointment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(appointment.getAppointmentDate().getTime()));
            pstmt.setString(2, appointment.getDescription());
            pstmt.setString(3, appointment.getAppointmentId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves an appointment from the database by ID
     */
    public static Appointment getAppointment(String appointmentId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, appointmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date appointmentDate = new Date(rs.getTimestamp("appointment_date").getTime());
                    String description = rs.getString("description");
                    
                    return new Appointment(appointmentId, appointmentDate, description);
                }
                return null;
            }
        }
    }

    /**
     * Retrieves all appointments from the database
     */
    public static List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String appointmentId = rs.getString("appointment_id");
                Date appointmentDate = new Date(rs.getTimestamp("appointment_date").getTime());
                String description = rs.getString("description");
                
                appointments.add(new Appointment(appointmentId, appointmentDate, description));
            }
        }
        
        return appointments;
    }

    /**
     * Deletes an appointment from the database by ID
     */
    public static void deleteAppointment(String appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, appointmentId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Checks if an appointment with the given ID exists
     */
    public static boolean appointmentExists(String appointmentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, appointmentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }
}
