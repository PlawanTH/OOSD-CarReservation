/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.CarManagement;

import Model.Car;
import Model.CarFactory;
import Model.DBConnector;
import View.CarManagement;
import controller.MainApplicationController;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Mel
 */
public class CarManagementController {
    
    // view : car management
    CarManagement carManagementWindow;
    // ArrayList : car list
    ArrayList<Car> car_list;
    
    // relation window
    CarAddingController carAddingController = null;
    CarDetailController carDetailController = null;
    
    public CarManagementController(){
        // run car management window
        carManagementWindow = new CarManagement(this);
    }
    
    // search car : ArrayList<Car>
    public ArrayList<Car> getSearchedCarList(){
        car_list = new ArrayList<Car>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string
        String sql = "SELECT * FROM oosd_g3_car_car"
                + " WHERE (Brand LIKE '%"+carManagementWindow.getSearchedText()+"%'"
                + " OR Series LIKE '%"+carManagementWindow.getSearchedText()+"%'"
                + " OR LicensePlate LIKE '%"+carManagementWindow.getSearchedText()+"%'"
                + " OR Year LIKE '%"+carManagementWindow.getSearchedText()+"%'"
                + " OR CarType LIKE '%"+carManagementWindow.getSearchedText()+"%')";
        
        if(carManagementWindow.isAvailableCar()){
            sql += " AND Status = 'Available'";
        } else if(carManagementWindow.isReservedCar()){
            sql += " AND Status = 'Reserved'";
        }
        
        ArrayList<HashMap> searched_item;
        searched_item = db.queryRows(sql);
        for(HashMap item : searched_item){
            car_list.add(new CarFactory().makeCar(item));
        }
        
        System.out.println(db.disconnect());
        
        System.out.println("get searched car list success.");
        
        return car_list;
    }
    
    // refresh
    public ArrayList<Car> getAllCarList(){
        car_list = new ArrayList<Car>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        String sql = "SELECT * FROM oosd_g3_car_car";
        
        ArrayList<HashMap> searched_item;
        searched_item = db.queryRows(sql);
        for(HashMap item : searched_item){
            car_list.add(new CarFactory().makeCar(item));
        }
        
        System.out.println(db.disconnect());
        
        System.out.println("get all car list success.");
        
        return car_list;
    }
    
    // table content
    public ArrayList<Object[]> getTableContent(ArrayList<Car> cars){
        ArrayList<Object[]> table_contents = new ArrayList<Object[]>();
        for(Car car : cars){
            int carID = car.getCarID();
            
            String licensePlate = car.getLicensePlate();
            String brand = car.getBrand();
            String series = car.getSeries();
            String year = car.getYear();
            String carType = car.getCarType();
            int engineSize = car.getEngineSize();
            String gearType = car.getGearType();
            String color = car.getColor();
            
            int passenger = car.getPassenger();
            double price = car.getPricePerDay();
            String status = car.getStatus();
            
            Object[] data = {carID, licensePlate, brand, series, year, carType, engineSize, gearType, color, passenger, price, status};
            table_contents.add(data);
        }
        System.out.println("get table content success.");
        return table_contents;
    }
    
    // add car
    public void runCarAddingWindow(){
        if(carAddingController == null){
            carAddingController = new CarAddingController(this);
        }
    }
    
    // detail
    public void runCarDetailWindow(){
        if(carDetailController == null){
            JTextField txtID = new JTextField();
            // create component with JTextField:id
            JComponent[] input = new JComponent[]{
                new JLabel("Enter Car ID", SwingConstants.CENTER),
                txtID
            };

            int n = JOptionPane.showConfirmDialog(null, input, "Update Reservation", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);

            String id_txt = txtID.getText();
            
            // if press OK
            if(n==JOptionPane.OK_OPTION){
                Car car = null;
                // parse String to int
                try {
                    Integer.parseInt(id_txt);
                    car = new CarFactory().findCar(Integer.parseInt(id_txt));
                } catch(Exception e) { 
                    car = null;
                }
            
                if( car != null ){
                    carDetailController = new CarDetailController(this, car);
                } else {
                    JOptionPane.showMessageDialog(null, new JComponent[]{new JLabel("Invalid Value or No Car ID.", SwingConstants.CENTER)},"Error Alert!" , JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, new JComponent[]{new JLabel("Invalid Value or No Car ID.", SwingConstants.CENTER)},"Error Alert!" , JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    // back to mainapp
    public void goToMainApplication(){
        new MainApplicationController();
        carManagementWindow.dispose();
        carManagementWindow = null;
        System.out.println("go to main application.");
    }   
    
    // window management
    public CarManagement getCarManagementWindow(){
        return carManagementWindow;
    }
    
    public void resetCarAddingWindow(){
        carAddingController = null;
    }
    
    public void resetCarDetailWindow(){
        carDetailController = null;
    }
    
}
