/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import edu.sit.cs.db.CSDbDelegate;


/**
 *
 * @author chalinyasutrat
 */
public class DBConnector extends CSDbDelegate {
    
    public DBConnector(){
        //super("csprog-in.sit.kmutt.ac.th", "3306", "CSC105_G6", "csc105_2014", "csc105");
        super("localhost", "3306", "csc319", "root", "");
    }
    
}
