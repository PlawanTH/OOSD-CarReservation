/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.CarManagement.CarManagementController;
import csc319_carreservation.MainApp;




/**
 *
 * @author chalinyasutrat
 */
public class MainApplicationController {
     // Main Application Window
    MainApp mainapp;
    
    public MainApplicationController(){
        // create main application window
        mainapp = new MainApp(this);
        System.out.println("Main Application is running.");
    }
    
    // run reservation window controller method (and dispose) **
    public void runReservationWindow(){
        mainapp.dispose();
        
        new ReservationController();
        System.out.println("Run Reservation Management System.");
        mainapp = null;
    }  
    
    

    // run car management window controller method (and dispose)
    public void runCarManagementWindow(){
        mainapp.dispose();
        mainapp = null;
        
        new CarManagementController();
        System.out.println("Run Car Management System.");
    }

    // run customer management window controller method (and dispose)
    
    // log out -- run login window controller (and dispose)
    public void logout(){
        mainapp.dispose();
        mainapp = null;
        new NewLoginController();
    }
    
}