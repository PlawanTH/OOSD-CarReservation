/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.reservation;

import Model.DBConnector;
import Model.Reservation;
import View.ReservationDetailWindow;
import View.ReservationUpdateWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author chalinyasutrat
 */
public class ReservationDetailController {
   
    // view : reservation detail view
    ReservationDetailWindow reservationDetailWindow = null;
    ReservationUpdateWindow reservationUpdateWindow = null;
    
    // model : Reservation
    Reservation reservation;
    
    // controller : from previous frame
    ReservationController controllerPrev;
    
    public ReservationDetailController(ReservationController controller, Reservation reservation){
        // run reservation detail view
        controllerPrev = controller;
        this.reservation = reservation;
        
        // run reservation adding window
        reservationDetailWindow = new ReservationDetailWindow(this);
        reservationDetailWindow.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e){
                reservationDetailWindow = null;
                controllerPrev.resetReservationDetailWindow();
                System.out.println("Detail Close");
            }
        });
    }
    
    // get reservation
    public Reservation getReservation(){
        return reservation;
    }
    
    // update + confirmation
    public void runReservationUpdateWindow(){
        //ReservationUpdateController reservationUpdateController = new ReservationUpdateController();
        if(reservationUpdateWindow == null){
            reservationUpdateWindow = new ReservationUpdateWindow(this, reservation);
            
            reservationDetailWindow.setVisible(false);
            
            reservationUpdateWindow.addWindowListener(new WindowAdapter(){
                public void windowClosed(WindowEvent e){
                    reservationDetailWindow.dispose();
                    reservationDetailWindow = null;
                    reservationUpdateWindow = null;
                    controllerPrev.getReservationManagementWindow().refresh();
                }
            });
        }
    }
    
    public void updateDetail(){
        if( !( reservationUpdateWindow.getPickUpdate().equals("") ||
                reservationUpdateWindow.getPickupLocation().equals("") ||
                reservationUpdateWindow.getReturnDate().equals("") ||
                reservationUpdateWindow.getReturnLocation().equals("") ||
                reservationUpdateWindow.getStatus().equals("") ) ){
            int n = JOptionPane.showConfirmDialog(null, "Confirm updating?", "Confirm Windows", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if( n == JOptionPane.YES_OPTION ){
                DBConnector db = new DBConnector();
                System.out.println(db.connect());
                String sql_update = "UPDATE oosd_g3_car_reservation SET PickUpDate = '"
                        + reservationUpdateWindow.getPickUpdate()
                        + "', ReturnDate = '"
                        + reservationUpdateWindow.getReturnDate()
                        + "', PickUpLocation = '"
                        + reservationUpdateWindow.getPickupLocation()
                        + "', ReturnLocation = '"
                        + reservationUpdateWindow.getReturnLocation()
                        + "', Status = '"
                        + reservationUpdateWindow.getStatus()
                        + "' WHERE ReservationID = " + reservation.getReservationID();

                System.out.println(db.executeQuery(sql_update));
                reservation.setPickUpDate(reservationUpdateWindow.getPickUpdate());
                reservation.setReturnDate(reservationUpdateWindow.getReturnDate());
                reservation.setPickUpLocation(reservationUpdateWindow.getPickupLocation());
                reservation.setReturnLocation(reservationUpdateWindow.getReturnLocation());
                reservation.setStatus(reservationUpdateWindow.getStatus());
                
                if(reservation.getStatus().equals("Complete")){
                    String sql = "UPDATE oosd_g3_car_car SET Status='Available' WHERE CarID ="+reservation.getCar().getCarID();
                    System.out.println(db.executeQuery(sql));
                }
                
                
                System.out.println(db.disconnect());
                
                reservationUpdateWindow.dispose();
                reservationUpdateWindow = null;
            }     
        } else {
            JOptionPane.showMessageDialog(null, "Invalid value. Please enter all value.");
        }
    }
    
    // delete + confirmation
    public void removeReservation(){
        int n = JOptionPane.showConfirmDialog(null, "Do you sure to remove this reservation?", "Confirm Windows", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if( n == JOptionPane.YES_OPTION ){
            DBConnector db = new DBConnector();
            System.out.println(db.connect());
            String sql_delete = "DELETE FROM oosd_g3_car_reservation WHERE ReservationID = "+reservation.getReservationID();
            System.out.println(db.executeQuery(sql_delete));
            String sql_update = "UPDATE oosd_g3_car_car SET Status='Available' WHERE CarID ="+reservation.getCar().getCarID();
            System.out.println(db.executeQuery(sql_update));
            System.out.println(db.disconnect());
            reservationDetailWindow.dispose();
            reservationDetailWindow = null;
            controllerPrev.getReservationManagementWindow().refresh();
        }
    }
    
}
