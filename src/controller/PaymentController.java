/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.CarManagement.CarManagementController;
import controller.reservation.ReservationController;
import Model.DBConnector;
import Model.Reservation;
import View.Payment;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author T.Chan
 */
public class PaymentController {
    
    Payment payment;
    Reservation reservation;
    
    public PaymentController(){
        payment = new Payment(this);
        payment.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent es){
                new MainApplicationController();
            }
        });
    }
    
    public static void main(String[] args){
        new PaymentController();
    }
    
    public Reservation getReservation(){
        return reservation;
    }
    
    /*public Reservation findReservation(int id){
        reservation = new Reservation().findReservation(id);
        return reservation;
    }*/
    
    DefaultTableModel model;
    JTable table;
    JTextField searchText;
    JButton searchButton;
    
    // Browser
    public void runReservationBrowser(){
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
        
        ArrayList<Object[]> table_content = getTableContent(getSearchedReservation(searchText.getText()));
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
                ArrayList<Object[]> table_content = getTableContent(getSearchedReservation(searchText.getText()));
                for(Object[] row_content : table_content){
                    model.addRow(row_content);
                }
                table.setModel(model);
            }
            
        });
        
        JComponent[] input = {panel1, panel2};
        
        int result = JOptionPane.showConfirmDialog(null, input, "Reservation Browser", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(result == JOptionPane.OK_OPTION){
            reservation = new Reservation().findReservation(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
            payment.initFieldValues();
        }
        
        System.out.println("Car Management window is running.");
        
    }
    
    // search : ArrayList<Reservation>
    public ArrayList<Reservation> getSearchedReservation(String searchText){
        ArrayList<Reservation> reservation_list = new ArrayList<Reservation>();
        
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        //String search_sql = "SELECT * FROM CAR_Reserve";
        String search_sql = "SELECT * FROM oosd_g3_car_reservation, oosd_g3_car_customer"
                + " WHERE oosd_g3_car_reservation.CustomerID = oosd_g3_car_customer.CustomerID";
        search_sql += " AND (oosd_g3_car_reservation.CustomerID LIKE '%"+searchText+"%'";
        search_sql += " OR Firstname LIKE '%"+searchText+"%'";
        search_sql += " OR Lastname LIKE '%"+searchText+"%'";
        search_sql += " OR CarID LIKE '%"+searchText+"%')";
        search_sql += " AND Status='Reserving'";
       
        // ReserveID, CustomerID, CarID, PickUp_Date, Return_Date, Location, Mileage, Status
        ArrayList<HashMap> search_item;
        search_item = db.queryRows(search_sql);
        for(HashMap item : search_item){
            reservation_list.add(new Reservation().makeReservation(item));
        }
        System.out.println(db.disconnect());
        
        System.out.println("get searched reservation list success.");
        
        return reservation_list;
    }
    
    // add content to table
    public ArrayList<Object[]> getTableContent(ArrayList<Reservation> reservation_list){
        ArrayList<Object[]> table_contents = new ArrayList<Object[]>();
        for(Reservation reservation : reservation_list){
            int reservation_id = reservation.getReservationID();
            
            int customer_id = reservation.getCustomer().getCustomerID();
            String customer_firstname = reservation.getCustomer().getFirstname();
            String customer_lastname = reservation.getCustomer().getLastname();
            
            int car_id = reservation.getCar().getCarID();
            String pickup_date = reservation.getPickUpDate();
            String return_date = reservation.getReturnDate();
            String pickup_location = reservation.getPickUpLocation();
            String return_location = reservation.getReturnLocation();
            String status = reservation.getStatus();
            Object[] data = {reservation_id, customer_id, customer_firstname, customer_lastname, car_id, pickup_date, return_date, pickup_location, return_location, status};
            table_contents.add(data);
        }
        System.out.println("get table content success.");
        return table_contents;
    }
    
    public void confirmPayment(){
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        Calendar cal = Calendar.getInstance();
        java.util.Date d = cal.getTime();
        java.sql.Date date = new java.sql.Date(d.getTime());
        
        
        String sql = "INSERT INTO oosd_g3_car_receipt(ReservationID, Date, PayType, TaxType, PaidStatus, Price) VALUES ("
                + reservation.getReservationID()+","
                + "'"+date.toString()+"',"
                + "'"+payment.getPayType()+"',"
                + "'NoTax',"
                + "1,"
                + Double.parseDouble(payment.getPrice())
                + ")";
        
        System.out.println(db.executeQuery(sql));
        
        sql = "UPDATE oosd_g3_car_car SET Status = 'Available' WHERE CarID = "+reservation.getCar().getCarID();
        System.out.println(db.executeQuery(sql));
        
        sql = "UPDATE oosd_g3_car_reservation SET Status = 'Complete' WHERE ReservationID = "+reservation.getReservationID();
        System.out.println(db.executeQuery(sql));
        
        System.out.println(db.disconnect());
        
        payment.dispose();
        payment = null;
        
        new MainApplicationController();
        
        JOptionPane.showMessageDialog(null, "Payment Complete");
    }
    
}
