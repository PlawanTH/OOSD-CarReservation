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
public class Car {
  
    private int carID;
    private String licensePlate;
    private String brand;
    private String series;
    private String year;
    private int engineSize;
    private String color;
    private int passenger;
    private String carType;
    private String gearType;
    private double pricePerDay;
    private String status;
    
    private String dateAdded;
    private String updateDate;
    private String updateTime;
    
    public Car(){
        
    }
    
    public Car(int carID){
        findCar(carID);
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(int engineSize) {
        this.engineSize = engineSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public Car findCar(int id){
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find car data
        String sql = "SELECT * FROM oosd_g3_car_car WHERE CarID = " + id;
        
        // put all cars that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> cars = db.queryRows(sql);
        
        // if users was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( cars.size() > 0 ){
            for(HashMap car : cars){
                carID = (Integer.parseInt((String)car.get("CarID")));
                licensePlate = ((String)car.get("LicensePage"));
                brand = ((String)car.get("Brand"));
                series = ((String)car.get("Series"));
                year = ((String)car.get("Year"));
                engineSize = (Integer.parseInt((String)car.get("EngineSize")));
                color = ((String)car.get("Color"));
                passenger = (Integer.parseInt((String)car.get("Passenger")));
                carType = ((String)car.get("CarType"));
                
                pricePerDay = (Double.parseDouble((String)car.get("PricePerDay")));
                status = ((String)car.get("Status"));
                
                dateAdded = ((String)car.get("DateAdded"));
                updateDate = ((String)car.get("UpdateDate"));
                updateTime = ((String)car.get("UpdateTime"));
            }
        } else {
            System.out.println(db.disconnect());
            return null;
        }
        
        System.out.println(db.disconnect());
        return this;
    }
    
    
    
    
    
    
    
    
    
}
