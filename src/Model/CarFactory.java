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
 * @author T.Chan
 */
public class CarFactory {
    
    public Car findCar(int id){
        Car car = null;
        
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find car data
        String sql = "SELECT * FROM oosd_g3_car_car WHERE CarID = " + id;
        
        // put all cars that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> items = db.queryRows(sql);
        
        // if users was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( db.countRows(items) > 0 ){
            car = new Car();
            for(HashMap item : items){
                car.setCarID(Integer.parseInt((String)item.get("CarID")));
                car.setLicensePlate((String)item.get("LicensePlate"));
                car.setBrand((String)item.get("Brand"));
                car.setSeries((String)item.get("Series"));
                car.setYear((String)item.get("Year"));
                car.setEngineSize(Integer.parseInt((String)item.get("EngineSize")));
                car.setColor((String)item.get("Color"));
                car.setPassenger(Integer.parseInt((String)item.get("Passenger")));
                car.setCarType((String)item.get("CarType"));
                car.setGearType((String)item.get("GearType"));
                
                car.setPricePerDay(Double.parseDouble((String)item.get("PricePerDay")));
                car.setStatus((String)item.get("Status"));
                
                car.setDateAdded((String)item.get("DateAdded"));
                car.setUpdateDate((String)item.get("UpdateDate"));
                car.setUpdateTime((String)item.get("UpdateTime"));
            }
        }/* else {
            System.out.println(db.disconnect());
            return null;
        }*/
        
        System.out.println(db.disconnect());
        return car;
    }
    
    public Car makeCar(HashMap h){
        /* ---------------------- fixed assigning value --------------------------- */
        Car car = new Car();
        car.setBrand(h.get("Brand").toString());
        car.setCarID(Integer.parseInt(h.get("CarID").toString()));
        car.setCarType(h.get("CarType").toString());
        car.setColor(h.get("Color").toString());
        car.setDateAdded(h.get("DateAdded").toString());
        car.setEngineSize(Integer.parseInt(h.get("EngineSize").toString()));
        car.setGearType(h.get("GearType").toString());
        car.setLicensePlate(h.get("LicensePlate").toString());
        car.setPassenger(Integer.parseInt(h.get("Passenger").toString()));
        car.setPricePerDay(Double.parseDouble(h.get("PricePerDay").toString()));
        car.setSeries(h.get("Series").toString());
        car.setStatus(h.get("Status").toString());
        car.setUpdateDate(h.get("UpdateDate").toString());
        car.setUpdateTime(h.get("UpdateTime").toString());
        car.setYear(h.get("Year").toString());
    
        return car;
    }
    
}
