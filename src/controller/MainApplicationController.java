/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
        System.out.println("Main Dispose.");
        mainapp = null;
    }  
}
    
    

    // run car management window controller method (and dispose)
    
    // run customer management window controller method (and dispose)
    
    // log out -- run login window controller (and dispose)
    