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
public class Customer extends Person {
    private int customerID;
    
    public Customer(){
        
    }
    
    public Customer(int id){
        findCustomer(id);
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    /**
     * 
     * @param id is an id of customer.
     * @return Customer if the customer was found it will return Customer object, otherwise return null. 
     */
    public Customer findCustomer(int id){
        
        // connect to DB
        DBConnector db = new DBConnector();
        System.out.println(db.connect());
        
        // sql query string to find customer data
        String sql = "SELECT * FROM oosd_g3_car_customer WHERE CustomerID = " + id;
        
        // put all customers that was found in DB to ArrayList in HashMap form
        ArrayList<HashMap> users = db.queryRows(sql);
        
        // if customers was found (row count > 0), assign value. Otherwise, disconnect DB and return null
        if( db.countRows(users) > 0 ){
            for(HashMap user : users){
                customerID = (Integer.parseInt((String)user.get("CustomerID")));
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
    
    public Customer makeCustomer(HashMap h){
        Customer customer = new Customer();
        
        customer.setPersonalData(h);
        customer.setCustomerID(Integer.parseInt(h.get("CustomerID").toString()));
        
        return customer;
    }
    
}
