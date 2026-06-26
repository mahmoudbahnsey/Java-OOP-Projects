/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager;

/**
 *
 * @author ahmed
 */
public class schedule_information {
    
    private String scheduleid ;
    private String  Scheduledate ;
    private String sceduletime ;
    
    
    public schedule_information(String scheduledate , String Schedudletime){
        this.Scheduledate=scheduledate;
        this.sceduletime=sceduletime;
    }

    public schedule_information(){
        
    }
    public String getScheduledate() {
        return Scheduledate;
    }

    public String getSceduletime() {
        return sceduletime;
    }
    
    
    
    
}



