/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.reservation;

import controller.CarManagement.CarManagementController;
import Model.DBConnector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import CarReserve.CarReserveAdd;
import View.ReservationAddingWindow;

/**
 *
 * @author chalinyasutrat
 */
public class ReservationAddingController {
    
    // view for reservation adding window
    ReservationAddingWindow reserveAddWindow;
    // model : Car
    // model : Customer
    // controller : Reservation Controller
    ReservationController controllerPrev;
    
    public ReservationAddingController(ReservationController controller){
        controllerPrev = controller;
        // run reservation adding window
        reserveAddWindow = new ReservationAddingWindow(this);
        reserveAddWindow.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e){
                reserveAddWindow = null;
                controllerPrev.resetReservationAddingWindow();
                System.out.println("Add Close");
            }
        });
    }
    
    // choose customer : dialog
    
    // choose car : dialog
    public void runCarManagementWindow(){
        new CarManagementController();
        System.out.println("Car Management window is running.");
    }
    
    // submit adding
    public void addSubmit(){
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        String sql_customer = "";
        ArrayList<HashMap> customers = null;
        boolean customerExist = false;
        
        if(reserveAddWindow.isSelectedOldCustomer()){
            if(!reserveAddWindow.getCitizenID1().equals("")){
                // find customer
                sql_customer = "SELECT * FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID();
                customers = db.queryRows(sql_customer);

                if(customers.size()==0){
                    JOptionPane.showMessageDialog(null, "Does not have customer ID. Try Again.");
                } else {
                    customerExist = true;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter customer ID.");
            }
        } else if(reserveAddWindow.isSelectedNewCustomer()){
            if( customerFieldNotEmpty() && reservationFieldNotEmpty() ){
                // find customer
                sql_customer = "SELECT * FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID();
                customers = db.queryRows(sql_customer);

                String sql_insert = "";
                // if not have customer on system, then add customer to system
                if(customers.size()==0){
                    sql_insert = "INSERT INTO oosd_g3_car_customer "
                            + "(`CitizenID`, `Firstname`, `Lastname`, `BirthDate`, `Address`, `Subdistrict`, `District`, `City`, `Country`, `ZipCode`, `Email`, `PhoneNumber`) VALUES ("
                            + "'" + reserveAddWindow.getCitizenID() + "',"
                            + "'" + reserveAddWindow.getFirstname()+ "',"
                            + "'" + reserveAddWindow.getLastname()+ "',"
                            + "'" + reserveAddWindow.getBirthdate()+ "',"
                            + "'" + reserveAddWindow.getAddress()+ "',"
                            + "'" + reserveAddWindow.getSubdistrict()+ "',"
                            + "'" + reserveAddWindow.getDistrict()+ "',"
                            + "'" + reserveAddWindow.getCity() + "',"
                            + "'" + reserveAddWindow.getCountry() + "',"
                            + "'" + reserveAddWindow.getZipCode()+ "',"
                            + "'" + reserveAddWindow.getEmail() + "',"
                            + "'" + reserveAddWindow.getPhoneNumber() + "'"
                            + ")";
                    boolean insertSuccess = db.executeQuery(sql_insert);
                    System.out.println(insertSuccess);
                    customerExist = true;

                    sql_customer = "SELECT CustomerID FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID();

                } else {
                    JOptionPane.showMessageDialog(null, "This citizen ID is exist.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
        
        if(customerExist){
            if(reservationFieldNotEmpty() && customerFieldNotEmpty()){
                // find car
                String carID = reserveAddWindow.getCarID();
                String sql = "SELECT CarID FROM oosd_g3_car_car WHERE CarID = " + carID + " AND Status='Available'";
                ArrayList<HashMap> cars = db.queryRows(sql);
                        
                if(cars.size() > 0){
                    customers = db.queryRows(sql_customer);
                    String currentCustomerID = "";
                    for(HashMap c : customers){
                        currentCustomerID = (String) c.get("CustomerID");
                    }
                    

                    // get date and turn to string
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    java.util.Date d = cal.getTime();
                    String date = new java.sql.Date(d.getTime()).toString();
                    String time = new java.sql.Time(d.getTime()).toString();

                    // create sql insert string
                    String sql_insert = "INSERT INTO oosd_g3_car_reservation "
                            + "(CustomerID, CarID, ReserveDate, ReserveTime, PickUpDate, PickUpLocation, ReturnDate, ReturnLocation, Status) VALUES ("
                            + currentCustomerID +","
                            + reserveAddWindow.getCarID()+","
                            + "'"+ date +"',"
                            + "'"+ time +"',"
                            + "'"+ reserveAddWindow.getPickUpDate() +"',"
                            + "'"+ reserveAddWindow.getPickUpLocation() +"',"
                            + "'"+ reserveAddWindow.getReturnDate() +"',"
                            + "'"+ reserveAddWindow.getReturnLocation() +"',"
                            + "'Pending'"
                            + ")";

                    // execute insert query
                    boolean insertSuccess = db.executeQuery(sql_insert);
                    System.out.println(insertSuccess);


                    // update car status to RESERVED
                    String sql_update = "UPDATE oosd_g3_car_car SET Status = 'Reserved' WHERE CarID = "+reserveAddWindow.getCarID();
                    System.out.println(db.executeQuery(sql_update));
                    //System.out.println(db.disconnect());
                    reserveAddWindow.dispose();
                    reserveAddWindow = null;
                } else {
                    JOptionPane.showMessageDialog(null, "No car, or car is reserved.", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
            JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
        
        
        /*
        // find car
        String carID = reserveAddWindow.getCarID();
        String sql = "SELECT CarID FROM oosd_g3_car_car WHERE CarID = " + carID + " AND Status='Available'";
        ArrayList<HashMap> cars = db.queryRows(sql);
        
        // check have a car and no empty input.
        if(cars.size() > 0){
            if( !(reserveAddWindow.getAddress().equals("") ||
                    reserveAddWindow.getBirthdate().equals("") ||
                    reserveAddWindow.getCarID().equals("") ||
                    reserveAddWindow.getCitizenID().equals("") ||
                    reserveAddWindow.getCity().equals("") ||
                    reserveAddWindow.getCountry().equals("") ||
                    reserveAddWindow.getDistrict().equals("") ||
                    reserveAddWindow.getEmail().equals("") ||
                    reserveAddWindow.getFirstname().equals("") ||
                    reserveAddWindow.getLastname().equals("") ||
                    reserveAddWindow.getPhoneNumber().equals("") ||
                    reserveAddWindow.getPickUpDate().equals("") ||
                    reserveAddWindow.getPickUpLocation().equals("") ||
                    reserveAddWindow.getReturnDate().equals("") ||
                    reserveAddWindow.getReturnLocation().equals("") ||
                    reserveAddWindow.getSubdistrict().equals("") ||
                    reserveAddWindow.getZipCode().equals("")
                    ) ){
                
                String sql_customer = "";
                ArrayList<HashMap> customers = null;
                boolean customerExist = false;
                
                if(reserveAddWindow.isSelectedOldCustomer()){
                    // find customer
                    sql_customer = "SELECT * FROM oosd_g3_car_customer WHERE CustomerID = " + reserveAddWindow.getCustomerID();
                    customers = db.queryRows(sql_customer);
                    
                    if(customers.size()==0){
                        JOptionPane.showMessageDialog(null, "Does not have customer ID. Try Again.");
                    } else {
                        customerExist = true;
                    }
                } else if(reserveAddWindow.isSelectedNewCustomer()){
                // find customer
                    sql_customer = "SELECT * FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID();
                    customers = db.queryRows(sql_customer);

                    String sql_insert = "";
                    // if not have customer on system, then add customer to system
                    if(customers.size()==0){
                        sql_insert = "INSERT INTO oosd_g3_car_customer "
                                + "(`CitizenID`, `Firstname`, `Lastname`, `DateOfBirth`, `Address`, `Subdistrict`, `District`, `City`, `Country`, `ZipCode`, `Email`, `PhoneNumber`) VALUES ("
                                + "'" + reserveAddWindow.getCitizenID() + "',"
                                + "'" + reserveAddWindow.getFirstname()+ "',"
                                + "'" + reserveAddWindow.getLastname()+ "',"
                                + "'" + reserveAddWindow.getBirthdate()+ "',"
                                + "'" + reserveAddWindow.getAddress()+ "',"
                                + "'" + reserveAddWindow.getSubdistrict()+ "',"
                                + "'" + reserveAddWindow.getDistrict()+ "',"
                                + "'" + reserveAddWindow.getCity() + "',"
                                + "'" + reserveAddWindow.getCountry() + "',"
                                + "'" + reserveAddWindow.getZipCode()+ "',"
                                + "'" + reserveAddWindow.getEmail() + "',"
                                + "'" + reserveAddWindow.getPhoneNumber() + "'"
                                + ")";
                        boolean insertSuccess = db.executeQuery(sql_insert);
                        System.out.println(insertSuccess);
                        customerExist = true;
                        
                        sql_customer = "SELECT CustomerID FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID();
                
                    } else {
                        JOptionPane.showMessageDialog(null, "This citizen ID is exist.");
                    }
                    
                }
                
                if(customerExist){
                
                    customers = db.queryRows(sql_customer);
                    String currentCustomerID = "";
                    for(HashMap customer : customers){
                        currentCustomerID = (String) customer.get("CustomerID");
                    }
                
                    // get date and turn to string
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    java.util.Date d = cal.getTime();
                    String date = new java.sql.Date(d.getTime()).toString();
                    String time = new java.sql.Time(d.getTime()).toString();

                    // create sql insert string
                    String sql_insert = "INSERT INTO oosd_g3_car_reservation "
                            + "(CustomerID, CarID, ReserveDate, ReserveTime, PickUpDate, PickUpLocation, ReturnDate, ReturnLocation, Status) VALUES ("
                            + currentCustomerID +","
                            + reserveAddWindow.getCarID()+","
                            + "'"+ date +"',"
                            + "'"+ time +"',"
                            + "'"+ reserveAddWindow.getPickUpDate() +"',"
                            + "'"+ reserveAddWindow.getPickUpLocation() +"',"
                            + "'"+ reserveAddWindow.getReturnDate() +"',"
                            + "'"+ reserveAddWindow.getReturnLocation() +"',"
                            + "'Pending'"
                            + ")";
                
                    // execute insert query
                    boolean insertSuccess = db.executeQuery(sql_insert);
                    System.out.println(insertSuccess);


                    // update car status to RESERVED
                    String sql_update = "UPDATE oosd_g3_car_car SET Status = 'Reserved' WHERE CarID = "+reserveAddWindow.getCarID();
                    System.out.println(db.executeQuery(sql_update));
                    //System.out.println(db.disconnect());
                    reserveAddWindow.dispose();
                    reserveAddWindow = null;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.", "Error", JOptionPane.PLAIN_MESSAGE);
                //System.out.println(db.disconnect());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No car, or car is reserved.", "Error", JOptionPane.PLAIN_MESSAGE);
            //System.out.println(db.disconnect());
        }
        */
        System.out.println(db.disconnect());
        controllerPrev.getReservationManagementWindow().refresh();
    }
    
    public boolean customerFieldNotEmpty(){
        return !(reserveAddWindow.getAddress().equals("") ||
                    reserveAddWindow.getBirthdate().equals("") ||
                    reserveAddWindow.getCitizenID().equals("") ||
                    reserveAddWindow.getCity().equals("") ||
                    reserveAddWindow.getCountry().equals("") ||
                    reserveAddWindow.getDistrict().equals("") ||
                    reserveAddWindow.getEmail().equals("") ||
                    reserveAddWindow.getFirstname().equals("") ||
                    reserveAddWindow.getLastname().equals("") ||
                    reserveAddWindow.getPhoneNumber().equals("") ||
                    reserveAddWindow.getSubdistrict().equals("") ||
                    reserveAddWindow.getZipCode().equals(""));
    }
    
    public boolean reservationFieldNotEmpty(){
        return !(reserveAddWindow.getCarID().equals("") ||
                    reserveAddWindow.getPickUpDate().equals("") ||
                    reserveAddWindow.getPickUpLocation().equals("") ||
                    reserveAddWindow.getReturnDate().equals("") ||
                    reserveAddWindow.getReturnLocation().equals(""));
    }
    
}
