package passengers;

import Database.Fetching;
import Database.Record;
import Database.Enhancement;
import javax.swing.JOptionPane;

public class PassengerProfile {
    
    public void updatePreferences(String passengerId, String seatPreference, 
                                String mealPreference, String specialAssistance) {
        try {
            String existingPassenger = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"passenger_id"},
                new String[] {"passenger_id"},
                passengerId);

            if (existingPassenger == null || existingPassenger.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Passenger not found.");
                return;
            }

            String[] columns = {"seat_preference", "meal_preference", "special_assistance"};
            String[] values = {seatPreference, mealPreference, specialAssistance};
            String[] conditions = {"passenger_id"};
            String[] conditionValues = {passengerId};

            Enhancement enhancer = new Enhancement("passenger_preferences", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Preferences updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating preferences: " + e.getMessage());
        }
    }

    public String[] getPreferences(String passengerId) {
        try {
            String[] preferences = new String[3];
            
            String seatPref = Fetching.fetchElementsFromDatabase_with_con("passenger_preferences",
                new String[] {"seat_preference"},
                new String[] {"passenger_id"},
                passengerId);
            
            String mealPref = Fetching.fetchElementsFromDatabase_with_con("passenger_preferences",
                new String[] {"meal_preference"},
                new String[] {"passenger_id"},
                passengerId);
            
            String specialAssist = Fetching.fetchElementsFromDatabase_with_con("passenger_preferences",
                new String[] {"special_assistance"},
                new String[] {"passenger_id"},
                passengerId);

            preferences[0] = seatPref;
            preferences[1] = mealPref;
            preferences[2] = specialAssist;

            return preferences;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving preferences: " + e.getMessage());
            return null;
        }
    }
    public void updateFrequentFlyerInfo(String passengerId, String frequentFlyerNumber, 
                       String airline, int miles) {
        try {
            String existingPassenger = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"passenger_id"},
                new String[] {"passenger_id"},
                passengerId);

            if (existingPassenger == null || existingPassenger.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Passenger not found.");
                return;
            }




            String[] columns = {"frequent_flyer_number", "airline", "miles"};
            String[] values = {frequentFlyerNumber, airline, String.valueOf(miles)};
            String[] conditions = {"passenger_id"};
            String[] conditionValues = {passengerId};

            Enhancement enhancer = new Enhancement("frequent_flyer_info", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Frequent flyer information updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating frequent flyer information: " + e.getMessage());
        }
    }

    // Get frequent flyer information
    public String[] getFrequentFlyerInfo(String passengerId) {
        try {
            String[] info = new String[3];
            
            String ffNumber = Fetching.fetchElementsFromDatabase_with_con("frequent_flyer_info",
                new String[] {"frequent_flyer_number"},
                new String[] {"passenger_id"},
                passengerId);
            
            String airline = Fetching.fetchElementsFromDatabase_with_con("frequent_flyer_info",
                new String[] {"airline"},
                new String[] {"passenger_id"},
                passengerId);
            
            String miles = Fetching.fetchElementsFromDatabase_with_con("frequent_flyer_info",
                new String[] {"miles"},
                new String[] {"passenger_id"},
                passengerId);

            info[0] = ffNumber;
            info[1] = airline;
            info[2] = miles;

            return info;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving frequent flyer information: " + e.getMessage());
            return null;
        }
    }

    // Update emergency contact information
    public void updateEmergencyContact(String passengerId, String contactName, 
                                     String contactPhone, String relationship) {
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

            // Update emergency contact information
            String[] columns = {"contact_name", "contact_phone", "relationship"};
            String[] values = {contactName, contactPhone, relationship};
            String[] conditions = {"passenger_id"};
            String[] conditionValues = {passengerId};

            Enhancement enhancer = new Enhancement("emergency_contact", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Emergency contact information updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating emergency contact information: " + e.getMessage());
        }
    }

    // Get emergency contact information
    public String[] getEmergencyContact(String passengerId) {
        try {
            String[] contact = new String[3];
            
            String name = Fetching.fetchElementsFromDatabase_with_con("emergency_contact",
                new String[] {"contact_name"},
                new String[] {"passenger_id"},
                passengerId);
            
            String phone = Fetching.fetchElementsFromDatabase_with_con("emergency_contact",
                new String[] {"contact_phone"},
                new String[] {"passenger_id"},
                passengerId);
            
            String relationship = Fetching.fetchElementsFromDatabase_with_con("emergency_contact",
                new String[] {"relationship"},
                new String[] {"passenger_id"},
                passengerId);

            contact[0] = name;
            contact[1] = phone;
            contact[2] = relationship;

            return contact;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving emergency contact information: " + e.getMessage());
            return null;
        }
    }
} 