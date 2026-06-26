/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passengers;

import java.util.ArrayList;

public class contactinformation extends informative{

    private String phonenumber ; 
    private String email ;
    ArrayList<String> contactinfo = new ArrayList<>() ;
    
public contactinformation(String phonenumber , String email  ){
    
    contactinfo.add(0, this.phonenumber = phonenumber);
    contactinfo.add(1, this.email = email );
    
}

public contactinformation(){
    
}

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getContactinfo() {
        for (String con : contactinfo){
            return  con ;
        }
        return "";
    }

    @Override
    public String getinformation() {
        
       
        return contactinfo.toString() ;
    }


    
}
