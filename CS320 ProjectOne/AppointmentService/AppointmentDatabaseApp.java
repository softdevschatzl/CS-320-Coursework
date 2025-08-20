package appointment;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.SQLException;

/**
 * Sample application demonstrating the database-enhanced appointment system
 */
public class AppointmentDatabaseApp {
    private static AppointmentService appointmentService;
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public static void main(String[] args) {
        try {
            // Initialize service
            appointmentService = new AppointmentService();
            
            boolean running = true;
            while (running) {
                displayMenu();
                int choice = getUserChoice();
                
                switch (choice) {
                    case 1:
                        addAppointment();
                        break;
                    case 2:
                        viewAppointment();
                        break;
                    case 3:
                        updateAppointment();
                        break;
                    case 4:
                        deleteAppointment();
                        break;
                    case 5:
                        listAllAppointments();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            
            // Close connections
            appointmentService.close();
            scanner.close();
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n--- Appointment Management System ---");
        System.out.println("1. Add new appointment");
        System.out.println("2. View appointment details");
        System.out.println("3. Update appointment");
        System.out.println("4. Delete appointment");
        System.out.println("5. List all appointments");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void addAppointment() {
        try {
            System.out.print("Enter appointment ID (max 10 chars): ");
            String id = scanner.nextLine();
            
            System.out.print("Enter appointment date (yyyy-MM-dd HH:mm): ");
            String dateStr = scanner.nextLine();
            Date date = dateFormat.parse(dateStr);
            
            System.out.print("Enter description (max 50 chars): ");
            String description = scanner.nextLine();
            
            appointmentService.addAppointment(id, date, description);
            System.out.println("Appointment added successfully!");
            
        } catch (IllegalArgumentException | ParseException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void viewAppointment() {
        try {
            System.out.print("Enter appointment ID: ");
            String id = scanner.nextLine();
            
            Appointment appointment = appointmentService.getAppointmentById(id);
            
            System.out.println("\nAppointment Details:");
            System.out.println("ID: " + appointment.getAppointmentId());
            System.out.println("Date: " + dateFormat.format(appointment.getAppointmentDate()));
            System.out.println("Description: " + appointment.getDescription());
            
        } catch (IllegalArgumentException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void updateAppointment() {
        try {
            System.out.print("Enter appointment ID: ");
            String id = scanner.nextLine();
            
            // Get the existing appointment
            Appointment appointment = appointmentService.getAppointmentById(id);
            
            System.out.println("Current date: " + dateFormat.format(appointment.getAppointmentDate()));
            System.out.print("Enter new date (yyyy-MM-dd HH:mm) or press Enter to keep current: ");
            String dateStr = scanner.nextLine();
            
            Date date = appointment.getAppointmentDate();
            if (!dateStr.trim().isEmpty()) {
                date = dateFormat.parse(dateStr);
            }
            
            System.out.println("Current description: " + appointment.getDescription());
            System.out.print("Enter new description or press Enter to keep current: ");
            String description = scanner.nextLine();
            
            if (description.trim().isEmpty()) {
                description = appointment.getDescription();
            }
            
            appointmentService.updateAppointment(id, date, description);
            System.out.println("Appointment updated successfully!");
            
        } catch (IllegalArgumentException | ParseException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void deleteAppointment() {
        try {
            System.out.print("Enter appointment ID: ");
            String id = scanner.nextLine();
            
            appointmentService.deleteAppointment(id);
            System.out.println("Appointment deleted successfully!");
            
        } catch (IllegalArgumentException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void listAllAppointments() {
        try {
            // Refresh the cache to ensure we have the latest data
            appointmentService.refreshCache();
            
            List<Appointment> appointments = AppointmentDatabase.getAllAppointments();
            
            if (appointments.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }
            
            System.out.println("\nAll Appointments:");
            System.out.println("----------------");
            
            for (Appointment appointment : appointments) {
                System.out.println("ID: " + appointment.getAppointmentId() + 
                                   " | Date: " + dateFormat.format(appointment.getAppointmentDate()) + 
                                   " | Description: " + appointment.getDescription());
            }
            
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
