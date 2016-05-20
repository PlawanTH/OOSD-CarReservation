/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Model.Employee;
import csc319_carreservation.Login;

/**
 *
 * @author chalinyasutrat
 */
public class NewLoginController {

    // depends on Login Windows
    private Login login;
    private Employee employee;
    
    public NewLoginController(){
        login = new Login(this);
        employee = new Employee();
    }
    
    public void verifyLogin(String usr, String pwd){
        if((employee = employee.verifyEmployee(usr, pwd)) != null){
            // go to the main application
            new MainApplicationController();
            login.dispose();
            login = null;
            employee = null;
            System.out.println("Go to main application.");
        }
        else {
            // recreate employee
            employee = new Employee();
            // show message dialog box
            login.wrongLoginAlert();
            System.out.println("Username or Password was wrong. Try again.");
            // reset value
        }
    }
    
    
    
    
    
    
    
    
    
    
    
}
