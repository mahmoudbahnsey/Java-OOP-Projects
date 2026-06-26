package passengers;

import Database.Fetching;
import Database.Record;
import Database.Enhancement;
import Database.remove;
import javax.swing.JOptionPane;

public class PassengerNotification {
    
    // Send notification to passenger
    public void sendNotification(String passengerId, String notificationType, 
                            String message, String priority) {
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

            // Record the notification
            String[] columns = {"passenger_id", "notification_type", "message", "priority", "status"};
            String[] values = {passengerId, notificationType, message, priority, "UNREAD"};

            Record notificationRecord = new Record("notifications", columns, values);
            JOptionPane.showMessageDialog(null, "Notification sent successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error sending notification: " + e.getMessage());
        }
    }

    // Get all notifications for a passenger
    public String[][] getNotifications(String passengerId) {
        try {
            // Get count of notifications
            String countStr = Fetching.fetchElementsFromDatabase_with_con("notifications",
                new String[] {"COUNT(*)"},
                new String[] {"passenger_id"},
                passengerId);

            if (countStr == null || countStr.equals("0")) {
                return new String[0][0];
            }

            int count = Integer.parseInt(countStr);
            String[][] notifications = new String[count][5];

            // Get notification details
            for (int i = 0; i < count; i++) {
                String notificationId = Fetching.fetchElementsFromDatabase_with_con("notifications",
                    new String[] {"notification_id"},
                    new String[] {"passenger_id"},
                    passengerId);

                String type = Fetching.fetchElementsFromDatabase_with_con("notifications",
                    new String[] {"notification_type"},
                    new String[] {"notification_id"},
                    notificationId);

                String message = Fetching.fetchElementsFromDatabase_with_con("notifications",
                    new String[] {"message"},
                    new String[] {"notification_id"},
                    notificationId);

                String priority = Fetching.fetchElementsFromDatabase_with_con("notifications",
                    new String[] {"priority"},
                    new String[] {"notification_id"},
                    notificationId);

                String status = Fetching.fetchElementsFromDatabase_with_con("notifications",
                    new String[] {"status"},
                    new String[] {"notification_id"},
                    notificationId);

                notifications[i][0] = notificationId;
                notifications[i][1] = type;
                notifications[i][2] = message;
                notifications[i][3] = priority;
                notifications[i][4] = status;
            }

            return notifications;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving notifications: " + e.getMessage());
            return new String[0][0];
        }
    }

    // Mark notification as read
    public void markNotificationAsRead(String passengerId, String notificationId) {
        try {
            // Check if notification exists
            String existingNotification = Fetching.fetchElementsFromDatabase_with_con("notifications",
                new String[] {"notification_id"},
                new String[] {"passenger_id", "notification_id"},
                passengerId, notificationId);

            if (existingNotification == null || existingNotification.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Notification not found.");
                return;
            }

            // Update notification status
            String[] columns = {"status"};
            String[] values = {"READ"};
            String[] conditions = {"passenger_id", "notification_id"};
            String[] conditionValues = {passengerId, notificationId};

            Enhancement enhancer = new Enhancement("notifications", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Notification marked as read!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error marking notification as read: " + e.getMessage());
        }
    }

    // Get unread notifications count
    public int getUnreadNotificationsCount(String passengerId) {
        try {
            String countStr = Fetching.fetchElementsFromDatabase_with_con("notifications",
                new String[] {"COUNT(*)"},
                new String[] {"passenger_id", "status"},
                passengerId, "UNREAD");

            if (countStr == null || countStr.isEmpty()) {
                return 0;
            }

            return Integer.parseInt(countStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error getting unread notifications count: " + e.getMessage());
            return 0;
        }
    }

    // Delete notification
    public void deleteNotification(String passengerId, String notificationId) {
        try {
            // Check if notification exists
            String existingNotification = Fetching.fetchElementsFromDatabase_with_con("notifications",
                new String[] {"notification_id"},
                new String[] {"passenger_id", "notification_id"},
                passengerId, notificationId);

            if (existingNotification == null || existingNotification.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Notification not found.");
                return;
            }

            // Delete the notification
            if (remove.deletedata("notifications", 
                new String[] {"passenger_id", "notification_id"}, 
                passengerId, notificationId)) {
                JOptionPane.showMessageDialog(null, "Notification deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete notification.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deleting notification: " + e.getMessage());
        }
    }
}