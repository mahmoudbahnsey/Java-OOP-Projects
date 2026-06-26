package Database;

import java.sql.*;
import javax.swing.JOptionPane;

public class Record {
    // الكلاس ده مسؤل عن ادخال بيانات في الداتا بيز

    PreparedStatement PRD;

    public Record(String tablename, String[] columns, String... values) {

        insert_data_to_data_base(tablename, columns, values);
    }

    public Record() {

    }

    private static String formatValue(String value) {
        if (value == null)
            return null;
        try {
            Double.parseDouble(value);
            return value;
        } catch (NumberFormatException e) {
            return "\"" + value + "\"";
        }
    }

    public void insert_data_to_data_base(String tablename, String[] columns, String... values) {
        Link connnection_data = new Link();
        connnection_data.Databaseconnection();
        StringBuffer manuplatestring = new StringBuffer();
        boolean first_value = true;
        for (String value : values) {

            if (first_value) {

                manuplatestring.append(formatValue(value));
                first_value = false;
            } else {
                manuplatestring.append(",");
                manuplatestring.append(formatValue(value));

            }
        }

        System.out.println(manuplatestring.toString());

        try {
            @SuppressWarnings("unused")
            boolean restrictioncolumns = true;
            if (columns.length == 0) {
                restrictioncolumns = false;

                PRD = connnection_data.con
                        .prepareStatement("insert into " + tablename + " values(" + manuplatestring.toString() + ")");
                PRD.execute();
                if (PRD !=null){
                JOptionPane.showMessageDialog(null, "successfully insert data to database");
                }else {
        JOptionPane.showMessageDialog(null, "there is an issue while we trying to insert data please recheck all fields are fill or check the information and try again");
                }

                System.out.println(PRD);
            } else {
                StringBuffer columns_pattern = new StringBuffer();
                boolean first_entry = true;
                for (String column : columns) {
                    if (first_entry) {
                        columns_pattern.append(column);
                        first_entry = false;
                    } else {
                        columns_pattern.append(",");
                        columns_pattern.append(column);
                    }
                }

                PRD = connnection_data.con.prepareStatement("insert into " + tablename + " "
                        + columns_pattern.toString() + " values(" + manuplatestring.toString() + ")");
                
                if (PRD.executeUpdate()!=0){
                    JOptionPane.showMessageDialog(null, "successfully insert data to database");
                }else {
        JOptionPane.showMessageDialog(null, "there is an issue while we trying to insert data please recheck all fields are fill or check the information and try again");

                }
            }

        } catch (Exception er) {
            System.out.println(er.getMessage());
        }
    }
}