package manager;
import java.util.*;

import Database.Fetching;
// import Database.Link;
// import Database.Record;
// import Database.remove;
// import Database.Enhancement;

// import java.sql.PreparedStatement;
// import java.time.*;
// import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

// import com.mysql.cj.x.protobuf.MysqlxCursor.Fetch;


public class schedulemanage {

    public void addTimeInformation(String time) {
        String fetchMaxId = Fetching.fetchElementsFromDatabase_with_con("time", new String[]{"max(time_id)"}, new String[]{});

        int newId = 1;
        if (fetchMaxId != null) {
            newId = Integer.parseInt(fetchMaxId) + 1;
        }

        Record insertTimeInformation = new Record("time", new String[]{}, String.valueOf(newId), time);
    }

    public void addDateInformation(String date) {
        String fetchMaxId = Fetching.fetchElementsFromDatabase_with_con("date", new String[]{"max(date_id)"}, new String[]{});

        if (Fetching.fetchElementsFromDatabase_with_con("date", new String[]{"date"}, new String[]{"date"}, date) != null) {
            JOptionPane.showMessageDialog(null, "This date already exists, please enter a new one and try again");
            return;
        }

        int newId = 1;
        if (fetchMaxId != null) {
            newId = Integer.parseInt(fetchMaxId) + 1;
        }

        Record insertDateInformation = new Record("date", new String[]{}, String.valueOf(newId), date);
    }

    public void addBaggageInformation() {
    }

    public void addPriceInformation(String price, String dateOfPrice) {
        String fetchMaxId = Fetching.fetchElementsFromDatabase_with_con("price", new String[]{"max(price_id)"}, new String[]{});

        if (Fetching.fetchElementsFromDatabase_with_con("price", new String[]{}, new String[]{"price"}, price) != null) {
            JOptionPane.showMessageDialog(null, "This price already exists, please enter a new one and try again");
            return;
        }

        int newId = 1;
        if (fetchMaxId != null) {
            newId = Integer.parseInt(fetchMaxId) + 1;
        }

        Record insertPriceInformation = new Record("price", new String[]{}, String.valueOf(newId), price, dateOfPrice);
    }
}

