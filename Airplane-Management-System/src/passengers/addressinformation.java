/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passengers;

import java.util.ArrayList;

/**
 *
 * @author ahmed
 */
public class addressinformation extends informative{
            
            private String city ;
            private String country ;
            private String Streetname ;
            private String floornumber ;
            private String apartementnumber ;
            
            ArrayList<String> information = new ArrayList<>() ;
    
    public addressinformation(String city , String country , String Streetname , 
            String floornumber , String apartementnumber){
        information.add(0, this.city = city );
        information.add(1, this.country = country);
        information.add(2 , this.Streetname = Streetname);
        information.add(3, this.floornumber=floornumber);
        information.add(4, this.apartementnumber=apartementnumber);
                
    }

    public addressinformation() {
    }
    
    
    
    
    
    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getStreetname() {
        return Streetname;
    }

    public String getFloornumber() {
        return floornumber;
    }

    public String getApartementnumber() {
        return apartementnumber;
    }

    @Override
    public String getinformation() {
        
        return information.toString() ;
    }


    
}
