package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.HashSet;

public class Fetching {
    static Link connection = new Link();
    static PreparedStatement PRD;
    static ResultSet rs;

    public Fetching(String tablename, String[] columns, String[] colum_conditions,
            String... value_of_conditions) {
    fetchElementsFromDatabase_with_con(tablename, columns, colum_conditions, value_of_conditions); 

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
    public static String patterndata(String tablename, String[] columns,
        String[] columns_condition, String... value_of_conditions) {

    StringBuilder patternofdata = new StringBuilder("select ");
    boolean first_entry = true;

    for (String col : columns) {
        if (first_entry) {
            patternofdata.append(" ").append(col);
            first_entry = false;
        } else {
            patternofdata.append(", ").append(col);
        }
    }
    patternofdata.append(" from ").append(tablename);

    // WHERE clause
    boolean where_started = false;
    HashSet<String> usedColumns = new HashSet<>();

    for (int i = 0; i < columns_condition.length; i++) {
        String currentCol = columns_condition[i];
        String value = (i < value_of_conditions.length) ? value_of_conditions[i] : null;

        if (usedColumns.contains(currentCol)) continue; // Avoid duplicates

        if (value != null && !value.isEmpty()) {
            // Use column=value
            if (!where_started) {
                patternofdata.append(" where ");
                where_started = true;
            } else {
                patternofdata.append(" and ");
            }
            patternofdata.append(currentCol).append("=").append(formatValue(value));
            usedColumns.add(currentCol);
        } else if (i + 1 < columns_condition.length) {
            String nextCol = columns_condition[i + 1];
            String nextValue = (i + 1 < value_of_conditions.length) ? value_of_conditions[i + 1] : null;

            if ((nextValue == null || nextValue.isEmpty()) &&
                !usedColumns.contains(currentCol) &&
                !usedColumns.contains(nextCol)) {

                if (!where_started) {
                    patternofdata.append(" where ");
                    where_started = true;
                } else {
                    patternofdata.append(" and ");
                }

                patternofdata.append(currentCol).append("=").append(nextCol);
                usedColumns.add(currentCol);
                usedColumns.add(nextCol);
                i++;
            }
        }
    }

    return patternofdata.toString();
}
public static String fetchElementsFromDatabase_with_con(String tablename, String[] columns,
        String[] columns_condition, String... value_of_conditions) {

    connection.Databaseconnection();
    StringBuilder allValues = new StringBuilder();

    try {
        PRD = connection.con.prepareStatement(patterndata(tablename, columns, columns_condition, value_of_conditions));
        rs = PRD.executeQuery();

        while (rs.next()) {
            for (String column : columns) {
                String value = rs.getString(column);
                allValues.append(value).append(","); 
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    if (allValues.length() > 0) {
        allValues.setLength(allValues.length() - 1);
    }

    return allValues.toString();
}


}
