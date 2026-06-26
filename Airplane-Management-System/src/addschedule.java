/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ahmed
 */
import Database.Fetching;
import Database.Record;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Arrays;
import java.util.*;
import javax.swing.text.DateFormatter;

public class addschedule extends javax.swing.JFrame {

    public addschedule() {
        initComponents();
        this.setSize(670, 450);
        this.setLocationRelativeTo(null);
//        String scheduletype = schedule_type.getSelectedItem().toString();
//        int baggage_weightt = Integer.parseInt(baggage.getToolTipText());
//         String timetype  = schedule_type.getSelectedItem().toString();

    }

    public String getdifference() {

        String[] depraturetimeinformation = gettime().split(":");
        String depraturetime = depraturetimeinformation[0];
        String[] arrivaltimeinformation = arrivaltime.getText().split(":");
        String estimatedarrivaltime = arrivaltimeinformation[0];

        int differencebetweentwotimes = Integer.parseInt(depraturetime) - Integer.parseInt(estimatedarrivaltime);
        if (differencebetweentwotimes < 0) {
            differencebetweentwotimes = Math.abs(differencebetweentwotimes);

        }

        return String.valueOf(differencebetweentwotimes);

    }

    int totalprice = 0;

    public void settotalprice() {

        String leveloftrip = class_type2.getSelectedItem().toString();

//        Random randomprices = new Random();
//        switch (leveloftrip) {
//
//            case "Economy Class":
//
//                if (Integer.parseInt(getdifference()) > 7) {
//
//                    totalprice = 400;
//                    calculatingfullpriceoftrip.setText(String.valueOf(totalprice));
//                } else if (Integer.parseInt(getdifference()) > 10 && Integer.parseInt(getdifference()) <= 20) {
//                    totalprice = 800;
//                    calculatingfullpriceoftrip.setText(String.valueOf(totalprice));
//                } else {
//                    totalprice = 300;
//                    calculatingfullpriceoftrip.setText(String.valueOf(totalprice));
//
//                }

//                totalprice = randomprices.nextInt(3400);
//                String price = String.valueOf(totalprice);
//                calculatingfullpriceoftrip.setText(price);
//                break;

//            case "Premium Economy":
//                totalprice = randomprices.nextInt(8400);
//                price = String.valueOf(totalprice);
//                calculatingfullpriceoftrip.setText(price);
//                break;
//            case "Business Class":
//                totalprice = randomprices.nextInt(15400);
//                price = String.valueOf(totalprice);
//                calculatingfullpriceoftrip.setText(price);
//                break;
//            default:
//                throw new AssertionError();
//        }
        

    }

    public String gettime() {
        String schedule_minutes = MINUTES.getSelectedItem().toString();
        String schedule_hours = HOURS.getSelectedItem().toString();

        LocalTime time = LocalTime.of(Integer.parseInt(schedule_hours), Integer.parseInt(schedule_minutes));
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("H:mm");
        String formattedTime = time.format(myFormatObj);

        return formattedTime;
    }

    public String getdate() {
        String schedulemonth = schedule_month.getSelectedItem().toString();
        String scheduledate = schedule_date.getSelectedItem().toString();
        LocalDate date = LocalDate.now();
        int year = date.getYear();

        String inputDate = year + "-" + schedulemonth + "-" + scheduledate;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate parsedDate = LocalDate.parse(inputDate, inputFormatter);
        return parsedDate.format(outputFormatter);
    }

    String backupitem = "";
    int backupindex = 0;
    boolean adjustingitems = false;

//
    public void calculateestmatedtimetoarrive(String currentcountry, String destenationcountry, String scheduleminutes, String schedulehours) {

        switch (currentcountry) {
            case "Egypt":
                switch (destenationcountry) {
                    case "Spain":
                        double destenation_country_distance = 928;
                        double final_arrival_time = destenation_country_distance / 900.0;

                        int flightHours = (int) final_arrival_time;
                        double fractionalPart = final_arrival_time - flightHours;
                        int flightMinutes = (int) (fractionalPart * 60);

                        int totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        int totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        String newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "France ":
                        destenation_country_distance = 3311;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "italy ":
                        destenation_country_distance = 3311;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Indonesia":
                        destenation_country_distance = 9364;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;
                }

            case "Indonesia":
                switch (destenationcountry) {
                    case "Spain":
                        double destenation_country_distance = 12368;
                        double final_arrival_time = destenation_country_distance / 900.0;

                        int flightHours = (int) final_arrival_time;
                        double fractionalPart = final_arrival_time - flightHours;
                        int flightMinutes = (int) (fractionalPart * 60);

                        int totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        int totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        String newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "France ":
                        destenation_country_distance = 11720;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "italy ":
                        destenation_country_distance = 11003;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Egypt":
                        destenation_country_distance = 9364;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                }

            case "Italy":
                switch (destenationcountry) {
                    case "Spain":
                        double destenation_country_distance = 1372;
                        double final_arrival_time = destenation_country_distance / 900.0;

                        int flightHours = (int) final_arrival_time;
                        double fractionalPart = final_arrival_time - flightHours;
                        int flightMinutes = (int) (fractionalPart * 60);

                        int totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        int totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        String newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "France ":
                        destenation_country_distance = 957;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Egypt ":
                        destenation_country_distance = 2357;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Indonesia":
                        destenation_country_distance = 11003;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                }

            case "Spain":
                switch (destenationcountry) {
                    case "France":
                        double destenation_country_distance = 801;
                        double final_arrival_time = destenation_country_distance / 900.0;

                        int flightHours = (int) final_arrival_time;
                        double fractionalPart = final_arrival_time - flightHours;
                        int flightMinutes = (int) (fractionalPart * 60);

                        int totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        int totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        String newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "France ":
                        destenation_country_distance = 826;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Egypt ":
                        destenation_country_distance = 3440;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Indonesia":
                        destenation_country_distance = 13217;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                }

            case "France":
                switch (destenationcountry) {
                    case "Spain":
                        double destenation_country_distance = 826;
                        double final_arrival_time = destenation_country_distance / 900.0;

                        int flightHours = (int) final_arrival_time;
                        double fractionalPart = final_arrival_time - flightHours;
                        int flightMinutes = (int) (fractionalPart * 60);

                        int totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        int totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        String newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Italy ":
                        destenation_country_distance = 826;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Egypt ":
                        destenation_country_distance = 3244;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                    case "Indonesia":
                        destenation_country_distance = 12543;
                        final_arrival_time = destenation_country_distance / 900.0;

                        flightHours = (int) final_arrival_time;
                        fractionalPart = final_arrival_time - flightHours;
                        flightMinutes = (int) (fractionalPart * 60);

                        totalMinutes = Integer.parseInt(scheduleminutes) + flightMinutes;
                        totalHours = Integer.parseInt(schedulehours) + flightHours + (totalMinutes / 60);
                        totalMinutes = totalMinutes % 60;

                        newTime = String.format("%02d:%02d", totalHours, totalMinutes);
                        arrivaltime.setText(newTime);
                        break;

                }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        schedule_type = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        HOURS = new javax.swing.JComboBox<>();
        schedule_month = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        destenationcountry = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        currentcountry = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        addschedule = new javax.swing.JButton();
        empty_fields_trigger = new javax.swing.JLabel();
        schedule_date = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        MINUTES = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        maxbaggageweight = new javax.swing.JComboBox<>();
        calculatingfullpriceoftrip = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        arrivaltime = new javax.swing.JTextField();
        class_type2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        airlineprovider = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("month");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 210, 40, 22);

        schedule_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MORNING", "NIGH", "AFTERNOON", "MIDNIGHT" }));
        schedule_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                schedule_typeActionPerformed(evt);
            }
        });
        jPanel1.add(schedule_type);
        schedule_type.setBounds(160, 70, 96, 22);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("schedule type");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(170, 40, 86, 16);

        HOURS.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", " ", " ", " ", " ", " ", " ", " ", " ", " ", " " }));
        jPanel1.add(HOURS);
        HOURS.setBounds(70, 50, 72, 22);

        schedule_month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        jPanel1.add(schedule_month);
        schedule_month.setBounds(70, 210, 60, 22);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("schedule time");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 10, 140, 22);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("TRIP INFORMATION");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(380, 10, 150, 20);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("SCHEDULE DATE");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(20, 130, 130, 20);

        destenationcountry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "France", "Italy", "Spain", "Indonesia", "Egypt", " " }));
        destenationcountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destenationcountryActionPerformed(evt);
            }
        });
        jPanel1.add(destenationcountry);
        destenationcountry.setBounds(530, 40, 110, 22);

        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        jLabel7.setText("FROM");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(330, 40, 37, 16);

        jLabel8.setText("TO");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(500, 40, 16, 16);

        currentcountry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "France", "Italy", "Spain", "Indonesia", "Egypt" }));
        currentcountry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentcountryMouseClicked(evt);
            }
        });
        currentcountry.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                currentcountryComponentMoved(evt);
            }
        });
        currentcountry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentcountryActionPerformed(evt);
            }
        });
        jPanel1.add(currentcountry);
        currentcountry.setBounds(370, 40, 110, 22);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("CLASS TYPE");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(380, 80, 70, 16);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("TRIP PROVIDER");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(520, 80, 90, 16);

        addschedule.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addschedule.setText("Register information");
        addschedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addscheduleActionPerformed(evt);
            }
        });
        jPanel1.add(addschedule);
        addschedule.setBounds(240, 310, 180, 40);

        empty_fields_trigger.setForeground(new java.awt.Color(255, 0, 51));
        empty_fields_trigger.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                empty_fields_triggerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jPanel1.add(empty_fields_trigger);
        empty_fields_trigger.setBounds(210, 300, 370, 30);

        schedule_date.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", " ", " ", " ", " ", " ", " ", " ", " ", " " }));
        jPanel1.add(schedule_date);
        schedule_date.setBounds(70, 170, 60, 22);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("MAX BAGGAGE WEIGHT");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(390, 210, 150, 22);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Day");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(20, 170, 30, 22);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("hours");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(20, 50, 40, 22);

        MINUTES.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", " ", " ", " ", " ", " ", " ", " ", " " }));
        MINUTES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MINUTESActionPerformed(evt);
            }
        });
        jPanel1.add(MINUTES);
        MINUTES.setBounds(70, 90, 72, 22);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("minutes");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(10, 90, 50, 22);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("FINAL PRICE");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(30, 260, 80, 22);

        maxbaggageweight.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", " ", " ", " ", " ", " ", " ", " ", " " }));
        maxbaggageweight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxbaggageweightActionPerformed(evt);
            }
        });
        jPanel1.add(maxbaggageweight);
        maxbaggageweight.setBounds(410, 240, 72, 22);

        calculatingfullpriceoftrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatingfullpriceoftripActionPerformed(evt);
            }
        });
        jPanel1.add(calculatingfullpriceoftrip);
        calculatingfullpriceoftrip.setBounds(20, 290, 110, 30);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("ESTEMATED ARRIVAL TIME");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(380, 140, 170, 22);

        arrivaltime.setBackground(new java.awt.Color(255, 255, 255));
        arrivaltime.setForeground(new java.awt.Color(0, 0, 0));
        arrivaltime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivaltimeActionPerformed(evt);
            }
        });
        jPanel1.add(arrivaltime);
        arrivaltime.setBounds(390, 170, 130, 22);

        class_type2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Economy Class", "Premium Economy", "Business Class", " ", " " }));
        class_type2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                class_type2ActionPerformed(evt);
            }
        });
        jPanel1.add(class_type2);
        class_type2.setBounds(360, 100, 120, 22);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(545, 333, 90, 30);

        airlineprovider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                airlineproviderActionPerformed(evt);
            }
        });
        jPanel1.add(airlineprovider);
        airlineprovider.setBounds(510, 100, 140, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        this.dispose();
        manager_dashboard man = new manager_dashboard();
        man.setLocationRelativeTo(null);
        man.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void class_type2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_class_type2ActionPerformed

        try {

            airlineprovider.removeAllItems();
            String current_country = currentcountry.getSelectedItem().toString();
            String destenation_country = destenationcountry.getSelectedItem().toString();
            String triplevel = class_type2.getSelectedItem().toString();

            String country_id = Fetching.fetchElementsFromDatabase_with_con("countries", new String[]{"country_id"}, new String[]{"country_name"}, current_country);
            String trip_id = Fetching.fetchElementsFromDatabase_with_con("trip_type", new String[]{"type_id"}, new String[]{"type_name"}, triplevel);
            String current_countryy = Fetching.fetchElementsFromDatabase_with_con("countries", new String[]{"country_name"}, new String[]{"country_name"}, current_country);
            String destinationcountryy = Fetching.fetchElementsFromDatabase_with_con("countries", new String[]{"country_name"}, new String[]{"country_name"}, destenation_country);

            if (current_countryy != null) {
                if (destinationcountryy != null) {
                    if (Fetching.fetchElementsFromDatabase_with_con("trip_type", new String[]{"type_name"}, new String[]{"type_name"}, triplevel.toString()) != null) {
                        switch (triplevel) {
                            case "Economy Class":
                                airlineprovider.removeAllItems();
                                String result = Fetching.fetchElementsFromDatabase_with_con(" airline_provider air , category  cat", new String[]{"air.airline_provider"}, new String[]{"cat.airline_provider_id", "air.airline_id", "cat.trip_type_id", "air.country_id"}, null, null, trip_id, country_id);
                                String[] items = result.split(",");

                                for (String infoprovider : items) {
                                    airlineprovider.addItem(infoprovider);
                                }

                                break;
                            case "Premium Economy":
                                airlineprovider.removeAllItems();
                                result = Fetching.fetchElementsFromDatabase_with_con(" airline_provider air , category  cat", new String[]{"air.airline_provider"}, new String[]{"cat.airline_provider_id", "air.airline_id", "cat.trip_type_id", "air.country_id"}, null, null, trip_id, country_id);
                                items = result.split(",");

                                for (String item : items) {
                                    airlineprovider.addItem(item);
                                }

                                break;
                            case "Business Class":

                                result = Fetching.fetchElementsFromDatabase_with_con(" airline_provider air , category  cat", new String[]{"air.airline_provider"}, new String[]{"cat.airline_provider_id", "air.airline_id", "cat.trip_type_id", "air.country_id"}, null, null, trip_id, country_id);
                                items = result.split(",");

                                for (String item : items) {
                                    airlineprovider.addItem(item);
                                }
                                break;

                            default:
                                throw new AssertionError();
                        }

                    }

                }
            }

        } catch (Exception er) {
            er.getMessage();
        }

    }//GEN-LAST:event_class_type2ActionPerformed

    private void arrivaltimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivaltimeActionPerformed

    }//GEN-LAST:event_arrivaltimeActionPerformed

    private void calculatingfullpriceoftripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatingfullpriceoftripActionPerformed

        settotalprice();

    }//GEN-LAST:event_calculatingfullpriceoftripActionPerformed

    private void maxbaggageweightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxbaggageweightActionPerformed
        int baggageweight = Integer.parseInt(maxbaggageweight.getSelectedItem().toString());
        String currentvalue = calculatingfullpriceoftrip.getText().trim();
        int totalpricee = Integer.parseInt(currentvalue) + totalprice;
        if (baggageweight <= 20 && baggageweight > 11) {
            totalprice += 500;
            calculatingfullpriceoftrip.setText(String.valueOf(totalpricee));

        } else if (baggageweight <= 30 && baggageweight > 20) {
            totalprice += 900;
            calculatingfullpriceoftrip.setText(String.valueOf(totalpricee));

        } else if (baggageweight > 30) {
            totalprice += 1200;
            calculatingfullpriceoftrip.setText(String.valueOf(totalpricee));

        }
    }//GEN-LAST:event_maxbaggageweightActionPerformed

    private void MINUTESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MINUTESActionPerformed

    }//GEN-LAST:event_MINUTESActionPerformed

    private void empty_fields_triggerAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_empty_fields_triggerAncestorAdded

    }//GEN-LAST:event_empty_fields_triggerAncestorAdded

    private void addscheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addscheduleActionPerformed

        String current_country = currentcountry.getSelectedItem().toString().trim();
        String deprature_country = destenationcountry.getSelectedItem().toString().trim();
        String tripprovider = airlineprovider.getSelectedItem().toString().trim();
        String tripprice = calculatingfullpriceoftrip.getText().trim();
        String maxbaggageamount = maxbaggageweight.getSelectedItem().toString().trim();
        String getscheduletime = gettime();
        String getdate = getdate();
        String scheduletype = schedule_type.getSelectedItem().toString().trim();
        String estmatedarrivaltime = arrivaltime.getText().trim();
        try{
        
            
            if (Fetching.fetchElementsFromDatabase_with_con("trip", new String [] {"*"}, new String [] {current_country , deprature_country }, new String [] {} ) != null){
                
                JOptionPane.showMessageDialog(null, "sorry we cant register this trip because the information was repeated , please assign new trip information ");
            
        }else {
                String initialid = "1";
                
                int maxid = Integer.parseInt(Fetching.fetchElementsFromDatabase_with_con("trip", new String [] {"max(trip_id"}, new String [] {}));
                
                if ( maxid > Integer.parseInt(initialid)){
                ++maxid ;
                 initialid = String.valueOf(maxid);
                 String baggagee_id = Fetching.fetchElementsFromDatabase_with_con("baggage", new String [] {"bagg_id"}, new String [] {"max_baggage"} , maxbaggageamount);
               Record tripinformation = new Record( "trip", new String [] {}, initialid , current_country , deprature_country, baggagee_id ) ;
                
               
               initialid = "1" ;
               int maxtrippriceid = Integer.parseInt(Fetching.fetchElementsFromDatabase_with_con("trip_price", new String [] {"max{triprice_id)"}, new String [] {}));
               if (maxtrippriceid > Integer.parseInt(initialid)){
                   ++maxtrippriceid ;
                   initialid = String.valueOf(maxtrippriceid);
                    int tripid = Integer.parseInt(Fetching.fetchElementsFromDatabase_with_con("trip", new String [] {"max(trip_id"}, new String [] {}));
            Record priceinformation = new Record("trip_price", new String [] {} ,initialid , tripprice , String.valueOf(tripid));

               }
                
               
                }
              
            }
            
            
        }catch(Exception er){
            System.out.println(er.getMessage());
        }
        
        
        
        
    }//GEN-LAST:event_addscheduleActionPerformed

    private void currentcountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentcountryActionPerformed

        String current_country = currentcountry.getSelectedItem().toString();

        currentcountry.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String current_country = currentcountry.getSelectedItem().toString();

                    if (!adjustingitems) {
                        for (int i = 0; i < destenationcountry.getItemCount(); i++) {
                            String item = destenationcountry.getItemAt(i);
                            if (item.equals(current_country)) {
                                backupitem = item;
                                backupindex = i;
                                destenationcountry.removeItemAt(i);
                                adjustingitems = true;
                                break;
                            }
                        }
                    } else {
                        destenationcountry.insertItemAt(backupitem, backupindex);
                        adjustingitems = false;
                    }
                }
            }
        });

    }//GEN-LAST:event_currentcountryActionPerformed

    private void currentcountryComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_currentcountryComponentMoved
        if (adjustingitems) {
            destenationcountry.insertItemAt(backupitem, backupindex);
        }
        adjustingitems = false;
    }//GEN-LAST:event_currentcountryComponentMoved

    private void currentcountryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_currentcountryMouseClicked

    }//GEN-LAST:event_currentcountryMouseClicked

    private void destenationcountryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_destenationcountryActionPerformed

    }//GEN-LAST:event_destenationcountryActionPerformed

    private void schedule_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_schedule_typeActionPerformed

    }//GEN-LAST:event_schedule_typeActionPerformed

    private void airlineproviderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_airlineproviderActionPerformed
settotalprice();
    }//GEN-LAST:event_airlineproviderActionPerformed
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addschedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addschedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addschedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addschedule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addschedule().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> HOURS;
    private javax.swing.JComboBox<String> MINUTES;
    private javax.swing.JButton addschedule;
    private javax.swing.JComboBox<String> airlineprovider;
    private javax.swing.JTextField arrivaltime;
    private javax.swing.JTextField calculatingfullpriceoftrip;
    private javax.swing.JComboBox<String> class_type2;
    private javax.swing.JComboBox<String> currentcountry;
    private javax.swing.JComboBox<String> destenationcountry;
    private javax.swing.JLabel empty_fields_trigger;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> maxbaggageweight;
    private javax.swing.JComboBox<String> schedule_date;
    private javax.swing.JComboBox<String> schedule_month;
    private javax.swing.JComboBox<String> schedule_type;
    // End of variables declaration//GEN-END:variables
}
