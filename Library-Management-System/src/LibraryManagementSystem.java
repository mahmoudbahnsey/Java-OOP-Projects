import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LibraryManagementSystem is the main entry point for the Library Management System application.
 * 
 * This class serves as the application launcher and initializes the main login interface.
 * It follows the standard Java application pattern with a main method that starts the
 * Swing-based user interface on the Event Dispatch Thread (EDT).
 * 
 * Key Features:
 * - Application entry point with main method
 * - Swing EDT initialization for thread-safe UI operations
 * - Clean application startup and shutdown
 * 
 * Application Flow:
 * 1. Main method is called by JVM
 * 2. SwingUtilities.invokeLater ensures UI runs on EDT
 * 3. LoginFrame is created and displayed
 * 4. User authentication and role-based dashboard access
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class LibraryManagementSystem {
    
    /**
     * Main method - Entry point for the Library Management System application.
     * This method initializes the Swing application and displays the login interface.
     * 
     * The application follows Swing best practices by ensuring all UI operations
     * run on the Event Dispatch Thread (EDT) for thread safety.
     * 
     * @param args Command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Add shutdown hook to ensure data is saved when app closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Saving data before shutdown...");
                LibraryManager.getInstance().saveData();
                System.out.println("Data saved successfully. Shutting down...");
            } catch (Exception e) {
                System.err.println("Error saving data during shutdown: " + e.getMessage());
            }
        }));
        
        // Ensure all Swing operations run on the Event Dispatch Thread (EDT)
        // This is crucial for thread safety in Swing applications
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and display the main login interface
                new LoginFrame();
                
                // Log successful application startup
                System.out.println("Library Management System started successfully.");
                System.out.println("Login interface displayed.");
                
            } catch (Exception e) {
                // Handle any startup errors gracefully
                System.err.println("Error starting Library Management System: " + e.getMessage());
                e.printStackTrace();
                
                // Display error dialog to user
                javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Failed to start Library Management System.\n" +
                    "Error: " + e.getMessage() + "\n\n" +
                    "Please check the console for more details.",
                    "Startup Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
                );
                
                // Exit application on critical error
                System.exit(1);
            }
        });
    }
    
    /**
     * Default constructor for LibraryManagementSystem.
     * This class is not designed to be instantiated directly.
     */
    public LibraryManagementSystem() {
        // This class serves as a utility class with static methods
        // No instance creation is intended
    }
    
    /**
     * Starts the application by creating the login interface.
     * This method provides an alternative way to start the application
     * programmatically rather than through the main method.
     * 
     * Note: This method is primarily for testing and programmatic access.
     * For normal usage, use the main method.
     */
    public void start() {
        // Delegate to the static main method logic
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame();
                System.out.println("Library Management System started via start() method.");
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
