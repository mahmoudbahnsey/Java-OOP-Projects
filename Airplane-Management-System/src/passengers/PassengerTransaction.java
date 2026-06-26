package passengers;

import Database.Fetching;
import Database.Record;
import Database.Enhancement;
import javax.swing.JOptionPane;

public class PassengerTransaction {
    
    public void makePayment(String passengerId, String scheduleId, double amount, String paymentMethod) {
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

            String existingPayment = Fetching.fetchElementsFromDatabase_with_con("payment",
                new String[] {"payment_id"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, scheduleId);

            if (existingPayment != null && !existingPayment.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Payment already made for this schedule.");
                return;
            }

            String[] columns = {"passenger_id", "schedule_id", "amount", "payment_method", "payment_status"};
            String[] values = {passengerId, scheduleId, String.valueOf(amount), paymentMethod, "COMPLETED"};

            Record paymentRecord = new Record("payment", columns, values);
            JOptionPane.showMessageDialog(null, "Payment completed successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error processing payment: " + e.getMessage());
        }
    }

    public String[][] getPaymentHistory(String passengerId) {
        try {
            String countStr = Fetching.fetchElementsFromDatabase_with_con("payment",
                new String[] {"COUNT(*)"},
                new String[] {"passenger_id"},
                passengerId);

            if (countStr == null || countStr.equals("0")) {
                return new String[0][0];
            }

            int count = Integer.parseInt(countStr);
            String[][] payments = new String[count][5];

            for (int i = 0; i < count; i++) {
                String scheduleId = Fetching.fetchElementsFromDatabase_with_con("payment",
                    new String[] {"schedule_id"},
                    new String[] {"passenger_id"},
                    passengerId);

                String amount = Fetching.fetchElementsFromDatabase_with_con("payment",
                    new String[] {"amount"},
                    new String[] {"passenger_id", "schedule_id"},
                    passengerId, scheduleId);

                String paymentMethod = Fetching.fetchElementsFromDatabase_with_con("payment",
                    new String[] {"payment_method"},
                    new String[] {"passenger_id", "schedule_id"},
                    passengerId, scheduleId);

                String paymentStatus = Fetching.fetchElementsFromDatabase_with_con("payment",
                    new String[] {"payment_status"},
                    new String[] {"passenger_id", "schedule_id"},
                    passengerId, scheduleId);

                String paymentDate = Fetching.fetchElementsFromDatabase_with_con("payment",
                    new String[] {"payment_date"},
                    new String[] {"passenger_id", "schedule_id"},
                    passengerId, scheduleId);

                payments[i][0] = scheduleId;
                payments[i][1] = amount;
                payments[i][2] = paymentMethod;
                payments[i][3] = paymentStatus;
                payments[i][4] = paymentDate;
            }

            return payments;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving payment history: " + e.getMessage());
            return new String[0][0];
        }
    }

    public void requestRefund(String passengerId, String scheduleId) {
        try {
            String existingPayment = Fetching.fetchElementsFromDatabase_with_con("payment",
                new String[] {"payment_id"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, scheduleId);

            if (existingPayment == null || existingPayment.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No payment found for this schedule.");
                return;
            }

            String[] columns = {"payment_status"};
            String[] values = {"REFUND_REQUESTED"};
            String[] conditions = {"passenger_id", "schedule_id"};
            String[] conditionValues = {passengerId, scheduleId};

            Enhancement enhancer = new Enhancement("payment", columns, conditions, conditionValues, values);
            JOptionPane.showMessageDialog(null, "Refund request submitted successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error requesting refund: " + e.getMessage());
        }
    }

    public String getPaymentStatus(String passengerId, String scheduleId) {
        try {
            String status = Fetching.fetchElementsFromDatabase_with_con("payment",
                new String[] {"payment_status"},
                new String[] {"passenger_id", "schedule_id"},
                passengerId, scheduleId);

            if (status == null || status.isEmpty()) {
                return "NO_PAYMENT_FOUND";
            }

            return status;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving payment status: " + e.getMessage());
            return "ERROR";
        }
    }
} 