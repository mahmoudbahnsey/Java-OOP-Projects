package Database;

import java.sql.*;
public class remove {

    static PreparedStatement prd ;
    
    public remove(String tablename , String[] columns , String...conditions){
        deletedata(tablename, columns, conditions);
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


    private static String Stringpattern( String [] columns , String...conditions){
        StringBuffer patternofvalues = new StringBuffer(" where  ");
        boolean first_entry = true ;
        int counter = 0 ;
        try{
            for (String col : columns){
                    if (first_entry){
                        patternofvalues.append(col+"="+formatValue(conditions[counter]));
                        first_entry = false ;
                    }else {
                        patternofvalues.append(" and ");
                        patternofvalues.append(col+"="+formatValue(conditions[++counter]));
                    }
                    
                }
                
            

        }catch(Exception er ){
            System.out.println(er.getMessage());
        }

        return patternofvalues.toString();

    }

    public static boolean deletedata(String tablename , String[] columns , String...conditions){

        Link connection = new Link() ;
        connection.Databaseconnection();

        try{

            prd =connection.con.prepareStatement("delete from " + tablename + Stringpattern(columns, conditions));
            if (prd.executeUpdate() != 0){
                return true ;
            }else {
                return false ;
            }
        }catch(Exception er ){
            er.getMessage() ;
        }
        return true ;

    }



}
