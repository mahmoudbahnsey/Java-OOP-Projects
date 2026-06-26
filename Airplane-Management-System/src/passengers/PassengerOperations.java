package passengers;

import Database.Fetching;
import Database.Record;
import Database.Enhancement;
import javax.swing.JOptionPane;

public class PassengerOperations {
public void updatePassengerInfo(String passengerId, String firstName, String lastName, 
String email, String phone) {
        try {
            // Check if passenger exists
            String existingPassenger = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"passenger_id"},
                new String[] {"passenger_id"},
                passengerId);

            if (existingPassenger == null || existingPassenger.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Passenger not found.");
                return;
            }

            
            String[] columns = {"first_name", "last_name", "email", "phone"};
            String[] values = {firstName, lastName, email, phone};
            String[] conditions = {"passenger_id"};
            String[] conditionValues = {passengerId};

            Enhancement enhancer = new Enhancement("passenger", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Passenger information updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating passenger information: " + e.getMessage());
        }
    }

    // Get passenger information
    public String[] getPassengerInfo(String passengerId) {
        try {
            String[] info = new String[5];
            
            // Get passenger details
            String firstName = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"first_name"},
                new String[] {"passenger_id"},
                passengerId);
            
            String lastName = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"last_name"},
                new String[] {"passenger_id"},
                passengerId);
            
            String email = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"email"},
                new String[] {"passenger_id"},
                passengerId);
            
            String phone = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"phone"},
                new String[] {"passenger_id"},
                passengerId);

            info[0] = firstName;
            info[1] = lastName;
            info[2] = email;
            info[3] = phone;
            info[4] = passengerId;

            return info;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving passenger information: " + e.getMessage());
            return null;
        }
    }

    // Change password
    public void changePassword(String passengerId, String oldPassword, String newPassword) {
        try {
            // Verify old password
            String currentPassword = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"password"},
                new String[] {"passenger_id", "password"},
                passengerId, oldPassword);

            if (currentPassword == null || currentPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Incorrect current password.");
                return;
            }

            // Update password
            String[] columns = {"password"};
            String[] values = {newPassword};
            String[] conditions = {"passenger_id"};
            String[] conditionValues = {passengerId};

            Enhancement enhancer = new Enhancement("passenger", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Password changed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error changing password: " + e.getMessage());
        }
    }
} 