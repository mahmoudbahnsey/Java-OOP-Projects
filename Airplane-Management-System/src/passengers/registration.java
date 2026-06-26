package passengers;
import java.util.*;

import Database.Fetching;
import Database.Record;
import javax.swing.*;
public class registration {


public registration(){
    
}


public void Signup(String Username , String Gender , String birthdate  , String phonenumber , String address ){


    if (Fetching.fetchElementsFromDatabase_with_con("users", new String [] {"username"}, new String [] {"username"} ,
     Username) != null){

        JOptionPane.showMessageDialog(null, "Soory but this username is exist before , so try enter new one and try again");

     }else {

        Record insertintodatabase = new Record("users", new String [] {}, Username ,Gender , birthdate , phonenumber , address);

     }
    

}


    public void sign_in(String username , String password){
     
        if (Fetching.fetchElementsFromDatabase_with_con("users", new String [] {"name","id"}, new String [] {"name","id"}, username , password) != null){
            JOptionPane.showMessageDialog(null, "Login Successfully ");
        }else {
         JOptionPane.showMessageDialog(null, " Sorry either username or password doesn't Exist please Recheck and try again");
        }

    }
    
} 
