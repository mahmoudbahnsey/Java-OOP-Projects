/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passengers ;

import java.util.ArrayList;

/**
 *
 * @author ahmed
 */
public class passengerinformation extends informative{
    
    private addressinformation addressinfo ;
    private contactinformation contactinfo ;
    
    private String Firstname  ;
    private String lastname ;
    private String age ;
    private String gender ;
    private  String nationality ;

    ArrayList <String> passinfo = new ArrayList<>() ;
    
    
    public passengerinformation(String Firstname , String lastname , String  age , String gender  , 
            String nationality  ){
        
        passinfo.add(0, this.Firstname = Firstname);
        passinfo.add(1, this.lastname = lastname );
        passinfo.add(2 , this.gender = gender) ;
        passinfo.add(3, this.age=age );
        passinfo.add(4, this.nationality = nationality );
        
        
    }
    
    
    
    public void craeteaddress(String city , String country , String Streetname , 
            String floornumber , String apartementnumber){
        
        addressinfo = new addressinformation(city, country, Streetname, floornumber, apartementnumber);
        
    }
    
    
    public void createcontact(String phonenumber , String email){
        contactinfo = new contactinformation(phonenumber, email) ;
        
    }
    

    public String getNationality() {
        return nationality;
    }

    public String getFirstname() {
        return Firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String getinformation() {
        
        
        System.out.println(addressinfo.getinformation()); ;
        System.out.println();
        System.out.println( contactinfo.getinformation() );
        
    return passinfo.toString() ;
    
}


}




class test{
    public static void main(String[] args) {
        passengerinformation pass = new passengerinformation("ahmed", "tarek", "12", "male", "egyption");
        
        pass.craeteaddress("giza", "egypt", "gardenia", "5 ", "356");
        pass.createcontact("01147768229", "ahmedyay532@gmail.com");
        
        pass.getinformation();
        
                
    }
}