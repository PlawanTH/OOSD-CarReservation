/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author chalinyasutrat
 */
public class Reservation {
    
    private int reservationID;
    private Car car;
    private Customer customer;
    
    private String reserveDate;
    private String reserveTime;
    
    private String pickUpDate;
    private String pickUpLocation;
    private String returnDate;
    private String returnLocation;
    
    private String status;
    
    public Reservation(){
        
    }

    public Reservation(int id){
        findReservation(id);
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(String pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Reservation findReservation(int id){
        
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find reservation data
        String sql = "SELECT * FROM oosd_g3_car_reservation WHERE ReservationID = " + id;
        
        // put all reservations that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> reservations = db.queryRows(sql);
        
        // if customers was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( db.countRows(reservations) > 0 ){
            for(HashMap reservation : reservations){
                car = new CarFactory().findCar(Integer.parseInt((String)reservation.get("CarID")));
                customer = new Customer(Integer.parseInt((String)reservation.get("CustomerID")));
                
                reservationID = (Integer.parseInt((String)reservation.get("ReservationID")));
                reserveDate = ((String)reservation.get("ReserveDate"));
                reserveTime = ((String)reservation.get("ReserveTime"));
                
                pickUpDate = ((String)reservation.get("PickUpDate"));
                pickUpLocation = ((String)reservation.get("PickUpLocation"));
                returnDate = ((String)reservation.get("ReturnDate"));
                returnLocation = ((String)reservation.get("ReturnLocation"));
                status = ((String)reservation.get("Status"));
            }
        } else {
            System.out.println(db.disconnect());
            return null;
        }
        
        System.out.println(db.disconnect());
        
        return this;
    }
    
    public Reservation makeReservation(HashMap h){
        /* ---------------------- fixed assigning value --------------------------- */
        Reservation reservation = new Reservation();
        reservation.setReservationID(Integer.parseInt((String)h.get("ReservationID")));
        reservation.setCustomer(new Customer(Integer.parseInt((String)h.get("CustomerID"))));
        reservation.setCar(new CarFactory().findCar(Integer.parseInt((String)h.get("CarID"))));
        
        reservation.setReserveDate((String)h.get("ReserveDate"));
        reservation.setReserveTime((String)h.get("ReserveTime"));
        
        reservation.setPickUpLocation((String)h.get("PickUpLocation"));
        reservation.setPickUpDate((String)h.get("PickUpDate"));
        reservation.setReturnLocation((String)h.get("ReturnLocation"));
        reservation.setReturnDate((String)h.get("ReturnDate"));
        
        reservation.setStatus((String)h.get("Status"));
    
        return reservation;
    }
    
}
