package passengers;

import Database.Fetching;
import Database.Record;
import Database.Enhancement;
import Database.remove;
import javax.swing.JOptionPane;

public class PassengerSchedule {
    
    public void bookSchedule(String passengerId, String scheduleId) {
        try {
            String existingPassenger = Fetching.fetchElementsFromDatabase_with_con("passenger",
                new String[] {"passenger_id"},
                new String[] {"passenger_id"},
                passengerId);

            if (existingPassenger == null || existingPassenger.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Passenger not found.");
                return;
            }

            String existingSchedule = Fetching.fetchElementsFromDatabase_with_con("schedule",
                new String[] {"schedule_id"},
                new String[] {"schedule_id"},
                scheduleId);

            if (existingSchedule == null || existingSchedule.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Schedule not found.");
                return;
            }

            String existingBooking = Fetching.fetchElementsFromDatabase_with_con("passenger_schedule",
                new String[] {"passenger_id"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, scheduleId);

            if (existingBooking != null && !existingBooking.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have already booked this schedule.");
                return;
            }

            String[] columns = {"passenger_id", "schedule_id"};
            String[] values = {passengerId, scheduleId};

            Record bookSchedule = new Record("passenger_schedule", columns, values);
            JOptionPane.showMessageDialog(null, "Schedule booked successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error booking schedule: " + e.getMessage());
        }
    }

    public void cancelSchedule(String passengerId, String scheduleId) {
        try {
            String existingBooking = Fetching.fetchElementsFromDatabase_with_con("passenger_schedule",
                new String[] {"passenger_id"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, scheduleId);

            if (existingBooking == null || existingBooking.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No booking found for this schedule.");
                return;
            }

            if (remove.deletedata("passenger_schedule", 
                new String[] {"passenger_id", "schedule_id"}, 
                passengerId, scheduleId)) {
                JOptionPane.showMessageDialog(null, "Schedule booking cancelled successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to cancel schedule booking.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error cancelling schedule: " + e.getMessage());
        }
    }

    public String[][] getPassengerSchedules(String passengerId) {
        try {
            String countStr = Fetching.fetchElementsFromDatabase_with_con("passenger_schedule",
                new String[] {"COUNT(*)"},
                new String[] {"passenger_id"},
                passengerId);

            if (countStr == null || countStr.equals("0")) {
                return new String[0][0];
            }

            int count = Integer.parseInt(countStr);
            String[][] schedules = new String[count][4];

            for (int i = 0; i < count; i++) {
                String scheduleId = Fetching.fetchElementsFromDatabase_with_con("passenger_schedule",
                    new String[] {"schedule_id"},
                    new String[] {"passenger_id"},
                    passengerId);

                String time = Fetching.fetchElementsFromDatabase_with_con("schedule",
                    new String[] {"schedule_time"},
                    new String[] {"schedule_id"},
                    scheduleId);

                String date = Fetching.fetchElementsFromDatabase_with_con("schedule",
                    new String[] {"schedule_date"},
                    new String[] {"schedule_id"},
                    scheduleId);

                String type = Fetching.fetchElementsFromDatabase_with_con("schedule",
                    new String[] {"schedule_type"},
                    new String[] {"schedule_id"},
                    scheduleId);

                schedules[i][0] = scheduleId;
                schedules[i][1] = time;
                schedules[i][2] = date;
                schedules[i][3] = type;
            }

            return schedules;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving passenger schedules: " + e.getMessage());
            return new String[0][0];
        }
    }

    public void changeSchedule(String passengerId, String oldScheduleId, String newScheduleId) {
        try {

            String existingBooking = Fetching.fetchElementsFromDatabase_with_con("passenger_schedule",
                new String[] {"passenger_id"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, oldScheduleId);

            if (existingBooking == null || existingBooking.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No booking found for the old schedule.");
                return;
            }

            String newSchedule = Fetching.fetchElementsFromDatabase_with_con("schedule",
                new String[] {"schedule_id"},
                new String[] {"schedule_id"},
                newScheduleId);

            if (newSchedule == null || newSchedule.isEmpty()) {
                JOptionPane.showMessageDialog(null, "New schedule not found.");
                return;
            }

            // Update the booking
            String[] columns = {"schedule_id"};
            String[] values = {newScheduleId};
            String[] conditions = {"passenger_id", "schedule_id"};
            String[] conditionValues = {passengerId, oldScheduleId};

            Enhancement enhancer = new Enhancement("passenger_schedule", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Schedule changed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error changing schedule: " + e.getMessage());
        }
    }
} 