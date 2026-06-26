package Database;

import java.sql.PreparedStatement;

// الكلاس ده مسؤل عن تعديل البيانات في الداتا بيز 

public class Enhancement {



    public Enhancement(String table, String[] columns, String[] condition_column,
    String[] condition_value,
    String...values){
        update_information(table, columns, condition_column, condition_value, values);
    }

    Link connection = new Link();
    PreparedStatement prd;

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

    // String[] condition_column
    // String[] condition_value,
    public String manipulation(String[] columns, String[] condition_column,
                            String[] condition_value,
                            String... values) {

    StringBuilder modifyingstring = new StringBuilder();

    // === SAFETY CHECK ===
    if (columns.length != values.length) {
        throw new IllegalArgumentException("Columns and values must have the same length.");
    }

    // Set column = value pairs
    for (int i = 0; i < columns.length; i++) {
        if (i > 0) modifyingstring.append(", ");
        modifyingstring.append(columns[i]).append("=").append(formatValue(values[i]));
    }

    // Add conditions (WHERE clause)
    for (int i = 0; i < condition_column.length; i++) {
        if (i == 0) {
            modifyingstring.append(" WHERE ");
        } else {
            modifyingstring.append(" AND ");
        }
        modifyingstring.append(condition_column[i]).append("=").append(formatValue(condition_value[i]));
    }

    
    return modifyingstring.toString();
    
}


    public void update_information(String table, String[] columns, String[] condition_column,
            String[] condition_value,
            String... values) {
                connection.Databaseconnection();
        try {
            prd = connection.con.prepareStatement("update " + table +
                    " set " + manipulation(columns, condition_column, condition_value, values));
            
            String ff = "update " + table +
                    " set " + manipulation(columns, condition_column, condition_value, values) ;
            System.out.println(ff);
                    if(prd.executeUpdate() !=0){
                        System.out.println("successfully performed the operation");
                    }else {
                        System.out.println("we cant do the operation , du to some issues please check the information again and try again ");
                    }
        } catch (Exception er) {
            System.out.println(er.getMessage());
        }

    }

}
