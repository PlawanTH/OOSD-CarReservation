/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.CarManagement;

import Model.DBConnector;
import View.CarAddingWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Mel
 */
public class CarAddingController {
    
    CarAddingWindow carAddWindow;
    CarManagementController controllerPrev;
    
    public CarAddingController(CarManagementController controller){
        controllerPrev = controller;
        
        // run car adding window
        carAddWindow = new CarAddingWindow(this);
        carAddWindow.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e){
                carAddWindow = null;
                controllerPrev.resetCarAddingWindow();
                controllerPrev.getCarManagementWindow().refresh();
                System.out.println("Add Closed");
                
            }
        });
    }
    
    // submit adding
    public void addSubmit(){
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // check not have empty fields
        if(!(carAddWindow.getBrand().equals("") ||
                carAddWindow.getSeries().equals("") ||
                carAddWindow.getYear().equals("") ||
                carAddWindow.getCarType().equals("") ||
                carAddWindow.getEngineSize().equals("") ||
                carAddWindow.getGearType().equals("") ||
                carAddWindow.getLicensePlate().equals("") ||
                carAddWindow.getColor().equals("") ||
                carAddWindow.getPassenger().equals("") ||
                carAddWindow.getPrice().equals("")) ){
            
            Calendar cal = Calendar.getInstance();
            java.util.Date d = cal.getTime();
            String date = new java.sql.Date(d.getTime()).toString();
            String time = new java.sql.Time(d.getTime()).toString();
                
            // create sql query string
            String sql = "INSERT INTO oosd_g3_car_car(LicensePlate, Brand, Series, EngineSize, Year, Color, Passenger, CarType, GearType, Status, DateAdded, UpdateDate, UpdateTime, PricePerDay) VALUES ("
                        + "'"+carAddWindow.getLicensePlate()+"',"
                        + "'"+carAddWindow.getBrand()+"',"
                        + "'"+carAddWindow.getSeries()+"',"
                        + Integer.parseInt(carAddWindow.getEngineSize())+","
                        + "'"+carAddWindow.getYear()+"',"
                        + "'"+carAddWindow.getColor()+"',"
                        + Integer.parseInt(carAddWindow.getPassenger())+","
                        + "'"+carAddWindow.getCarType()+"',"
                        + "'"+carAddWindow.getGearType()+"',"
                        + "'Available',"
                        + "'"+date+"',"
                        + "'"+date+"',"
                        + "'"+time+"',"
                        + Double.parseDouble(carAddWindow.getPrice())
                        + ")";
            // execute query
            boolean updateSuccess = db.executeQuery(sql);
            System.out.println(updateSuccess);
            System.out.println(db.disconnect());
            
            carAddWindow.dispose();
            carAddWindow = null;
        }
        else {
            JOptionPane.showMessageDialog(null, "Invalid or missed input. Try Again.");
        }
    }
    
}
