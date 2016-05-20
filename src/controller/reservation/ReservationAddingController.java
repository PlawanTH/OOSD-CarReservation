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

/**
 *
 * @author chalinyasutrat
 */
public class ReservationAddingController {
    
    // view for reservation adding window
    CarReserveAdd reserveAdd = null;
    // model : Car
    // model : Customer
    
    private int reservedID;
    
    public ReservationAddingController(){
        // run reservation adding window
        /*if(reserveAdd == null){
            reserveAdd = new CarReserveAdd();
            reserveAdd.addWindowListener(new WindowAdapter() {
                public void windonClosing(WindowEvent e){
                    reserveAdd = null;
                }
            });
        }*/
        reserveAdd = new CarReserveAdd(this);
        reservedID = getCurrentReservedID();
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
        String carID = reserveAdd.getCarID();
        String sql = "SELECT ID FROM CAR_Car WHERE ID='"+carID+"' AND Status='Available'";
        ArrayList<HashMap> cars = db.queryRows(sql);
        // check have a car and no empty input.
        if(cars.size() > 0){
            if( !(reserveAdd.getLastname().equals("") ||
                    reserveAdd.getName().equals("") ||
                    reserveAdd.getAge().equals("") ||
                    reserveAdd.getAddress().equals("") ||
                    reserveAdd.getCity().equals("") ||
                    reserveAdd.getCountry().equals("") ||
                    reserveAdd.getEmail().equals("") ||
                    reserveAdd.getPhoneNumber().equals("") ||
                    reserveAdd.getCarID().equals("") ||
                    reserveAdd.getPickUpDate().equals("") ||
                    reserveAdd.getReturnDate().equals("") ||
                    reserveAdd.getCustomerID().equals("") ||
                    reserveAdd.getLocation().equals("")
                    ) ){
                // create sql insert string
                String sql_insert = "INSERT INTO CAR_Reserve VALUES("
                        + reservedID
                        + ",'"
                        + reserveAdd.getCustomerID() + "','"
                        + reserveAdd.getCarID() + "','"
                        + reserveAdd.getPickUpDate() + "','"
                        + reserveAdd.getReturnDate() + "','"
                        + reserveAdd.getPickLocation() + "','"
                        + 0 + "','"
                        + "Pending"
                        + "')";
                // execute insert query
                boolean insertSuccess = db.executeQuery(sql_insert);
                System.out.println(insertSuccess);

                // find customer
                String sql_customer = "SELECT * FROM CAR_Customer WHERE ID='"+reserveAdd.getCustomerID()+"'";
                ArrayList<HashMap> customers = db.queryRows(sql_customer);
                // if not have customer on system, then add customer to system
                if(customers.size()==0){
                    sql_insert = "INSERT INTO CAR_Customer VALUES('"
                            + reserveAdd.getCustomerID() + "','"
                            + reserveAdd.getName() + "','"
                            + reserveAdd.getLastname() + "',"
                            + reserveAdd.getAge() + ",'"
                            + reserveAdd.getAddress() + "','"
                            + reserveAdd.getCity() + "','"
                            + reserveAdd.getCountry() + "','"
                            + reserveAdd.getEmail() + "','"
                            + reserveAdd.getPhoneNumber() + "')";
                    insertSuccess = db.executeQuery(sql_insert);
                    System.out.println(insertSuccess);
                }
                // update car status to RESERVED
                String sql_update = "UPDATE CAR_Car SET Status = 'Reserved' WHERE ID = '"+reserveAdd.getCarID()+"'";
                System.out.println(db.executeQuery(sql_update));
                //System.out.println(db.disconnect());
                reserveAdd.dispose();
                reserveAdd = null;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.", "Error", JOptionPane.PLAIN_MESSAGE);
                //System.out.println(db.disconnect());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No Car, or Car is reserved.", "Error", JOptionPane.PLAIN_MESSAGE);
            //System.out.println(db.disconnect());
        }
        System.out.println(db.disconnect());
    }
    
    // get current reserve id
    public int getCurrentReservedID(){
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        String sql = "SELECT ReserveID FROM CAR_Reserve ORDER BY ReserveID DESC LIMIT 1";
        HashMap reserveID = db.queryRow(sql);
        int currentID;
        try {
            currentID = Integer.parseInt((String)(reserveID.get("ReserveID")));
        } catch(Exception e){
            currentID = 0;
        }
        System.out.println(db.disconnect());
        
        return currentID+1;
    }
    
}
