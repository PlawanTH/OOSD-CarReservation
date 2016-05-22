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
import Model.Car;
import Model.CarFactory;
import View.ReservationAddingWindow;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

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
    
    DefaultTableModel model;
    JTable table;
    JTextField searchText;
    JButton searchButton;

    // choose car : dialog
    public void runCarBrowser(){
        //new CarManagementController();
        
        
        JPanel panel2 = new JPanel();
        searchText = new JTextField(15);
        searchButton = new JButton("Search");
        panel2.add(new JLabel("Search : "));
        panel2.add(searchText);
        panel2.add(searchButton);
        
        
        JPanel panel1 = new JPanel();
        
        String[] col = {"Reserve ID", "Customer ID", "Firstname", "Lastname", "Car ID", "Pick-up Date", "Return Date", "Pick-up Location", "Return Location", "Status"};

        model = new DefaultTableModel(col, 0){
            public boolean isCellEditable(int row, int col){
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setPreferredSize(new Dimension(800,350));
        panel1.add(scrollTable);
        
        ArrayList<Object[]> table_content = getTableContent(getSearchedCar(searchText.getText()));
        for(Object[] row_content : table_content){
            model.addRow(row_content);
        }
        table.setModel(model);
        
        
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                model = new DefaultTableModel(col, 0){
                    public boolean isCellEditable(int row, int col){
                        return false;
                    }
                };
                ArrayList<Object[]> table_content = getTableContent(getSearchedCar(searchText.getText()));
                for(Object[] row_content : table_content){
                    model.addRow(row_content);
                }
                table.setModel(model);
            }
            
        });
        
        JComponent[] input = {panel1, panel2};
        
        int result = JOptionPane.showConfirmDialog(null, input, "Car Browser", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION){
            reserveAddWindow.setCarID(table.getValueAt(table.getSelectedRow(), 0).toString());
        }
        
        System.out.println("Car Management window is running.");
        
    }
    
    public ArrayList<Car> getSearchedCar(String searchText){
        ArrayList car_list = new ArrayList<Car>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string
        String sql = "SELECT * FROM oosd_g3_car_car"
                + " WHERE (Brand LIKE '%"+searchText+"%'"
                + " OR Series LIKE '%"+searchText+"%'"
                + " OR LicensePlate LIKE '%"+searchText+"%'"
                + " OR Year LIKE '%"+searchText+"%'"
                + " OR CarType LIKE '%"+searchText+"%')"
                + " AND Status = 'Available'";
        
        ArrayList<HashMap> searched_item;
        searched_item = db.queryRows(sql);
        for(HashMap item : searched_item){
            car_list.add(new CarFactory().makeCar(item));
        }
        
        System.out.println(db.disconnect());
        
        System.out.println("get searched car list success.");
        
        return car_list;
    }
    
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
    
    // submit adding
    public void addSubmit(){
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        String sql_customer = "";
        ArrayList<HashMap> customers = null;
        boolean customerExist = false;
        boolean newCustomer = false;
        
        if(reserveAddWindow.isSelectedOldCustomer()){
            if(!reserveAddWindow.getCitizenID1().equals("")){
                // find customer
                sql_customer = "SELECT * FROM oosd_g3_car_customer WHERE CitizenID = " + reserveAddWindow.getCitizenID1();
                customers = db.queryRows(sql_customer);

                if(customers.size()==0){
                    JOptionPane.showMessageDialog(null, "Does not have customer ID. Try Again.");
                } else {
                    customerExist = true;
                    newCustomer = false;
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
                    newCustomer = true;
                    customerExist = true;
                } else {
                    JOptionPane.showMessageDialog(null, "This citizen ID is exist.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.", "Error", JOptionPane.PLAIN_MESSAGE);
            }
        }
        
        if(customerExist){
            if(reservationFieldNotEmpty()){
                
                // find car
                String carID = reserveAddWindow.getCarID();
                String sql = "SELECT CarID FROM oosd_g3_car_car WHERE CarID = " + carID + " AND Status='Available'";
                ArrayList<HashMap> cars = db.queryRows(sql);
                        
                if(cars.size() > 0){
                    if(newCustomer){
                        String sql_insert = "INSERT INTO oosd_g3_car_customer "
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

                    }
                    
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
