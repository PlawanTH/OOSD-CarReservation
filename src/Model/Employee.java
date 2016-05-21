/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import edu.sit.cs.db.CSDbDelegate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author chalinyasutrat
 */
public class Employee extends Person {

    private String username;
    
    public Employee(){
        
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String usr){
        username = usr;
    }
    
    public Employee findEmployee(String usr){
        
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find employee data
        String sql = "SELECT * FROM oosd_g3_car_employee WHERE Username = '" + usr + "'";
        
        // put all users that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> users = db.queryRows(sql);
        
        // if users was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( db.countRows(users) > 0 ){
            // assign value to employee
            for(HashMap user : users){
                username = ((String)user.get("Username"));
                setPersonalData(user);
            }
        } else {
            System.out.println(db.disconnect());
            return null;
        }
        
        // disconnect DB
        System.out.println(db.disconnect());
        
        // return this object
        return this;
    }
    
    public Employee verifyEmployee(String usr, String pwd){
        
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find employee data
        String sql = "SELECT * FROM oosd_g3_car_employee WHERE Username = '" + usr + "' AND Password = '" + pwd + "'";
        
        // put all users that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> users = db.queryRows(sql);
        
        // if users was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( db.countRows(users) > 0 ){
            // assign value to employee
            for(HashMap user : users){
                username = ((String)user.get("Username"));
                setPersonalData(user);
                return this;
            }
        }
        // disconnect DB
        System.out.println(db.disconnect());
        
        return null;
    }
    
}
