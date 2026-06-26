
package Database;
import java.sql.*;
public class Link {
    public Connection con ;
public void Databaseconnection(){
 // الكلاس ده مسؤل عن عمل اتصال بالداتا بيز 
    try{
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airlinebookingsystem","root","rootAsdF@#$");

        if (con !=null){
            System.out.println("successfully connected to the database");
        }else{
            System.out.println("there is a problem When trying to connect to the database ");
        }

    }catch(Exception er ){
        System.out.println(er.getMessage());
    }
}


}