/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.CarManagement;

import Model.Car;
import Model.DBConnector;
import View.CarDetailWindow;
import View.CarUpdateWindow;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Mel
 */
public class CarDetailController {
    
    CarDetailWindow carDetailWindow = null;
    CarUpdateWindow carUpdateWindow = null;
    
    Car car;
    
    CarManagementController controllerPrev;
    
    public CarDetailController(CarManagementController controller, Car car){
        controllerPrev = controller;
        this.car = car;
        
        carDetailWindow = new CarDetailWindow(this);
        carDetailWindow.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e){
                carDetailWindow = null;
                controllerPrev.resetCarDetailWindow();
                System.out.println("Detail Close");
            }
        });
    }
    
    // edit car detail
    public void editCarDetail(){
        if(car.getStatus().equals("Available")){
            JPanel panel_main = new JPanel();
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            JTextField txt_licensePlate = new JTextField(10);
            JTextField txt_price = new JTextField(10);

            txt_licensePlate.setText(car.getLicensePlate());
            txt_price.setText(""+car.getPricePerDay());

            panel1.add(new JLabel("License Plate:"));
            panel1.add(txt_licensePlate);

            panel2.add(new JLabel("Price per Day:"));
            panel2.add(txt_price);

            panel_main.setLayout(new GridLayout(2, 1));
            panel_main.add(panel1);
            panel_main.add(panel2);

            // create component with JTextField:id
            JComponent[] input = new JComponent[]{
                //new JLabel("Edit Car Detial", SwingConstants.CENTER),
                panel_main
            };

            int result = JOptionPane.showConfirmDialog(null, input, "Edit Car Detail", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(result == JOptionPane.OK_OPTION){
                if( !(txt_licensePlate.getText().equals("") || 
                        txt_price.getText().equals("")) ){
                    DBConnector db = new DBConnector();
                    System.out.println(db.connect());
                    
                    car.setLicensePlate(txt_licensePlate.getText());
                    car.setPricePerDay(Double.parseDouble(txt_price.getText()));

                    String sql = "UPDATE oosd_g3_car_car SET"
                            + " LicensePlate = '" + car.getLicensePlate() + "',"
                            + " PricePerDay = " + car.getPricePerDay()
                            + " WHERE CarID = " + car.getCarID();
                    
                    boolean updateSuccess = db.executeQuery(sql);
                    System.out.println(updateSuccess);
                    
                    System.out.println(db.disconnect());
                    
                    carDetailWindow.initFieldValue();
                    controllerPrev.getCarManagementWindow().refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cannot edit. The car is reserving.");
        }
    }
    
    // remove
    public void removeCar(){
        if(car.getStatus().equals("Available")){
            int n = JOptionPane.showConfirmDialog(null, "Do you sure to remove this reservation?", "Confirm Windows", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if( n == JOptionPane.YES_OPTION ){
                DBConnector db = new DBConnector();
                System.out.println(db.connect());
                String sql_delete = "DELETE FROM oosd_g3_car_car WHERE CarID = "+car.getCarID();
                System.out.println(db.executeQuery(sql_delete));
                System.out.println(db.disconnect());
                carDetailWindow.dispose();
                carDetailWindow = null;
                controllerPrev.getCarManagementWindow().refresh();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cannot delete. The car is reserving.");
        }
    }
    
    public Car getCar(){
        return car;
    }
    
}
