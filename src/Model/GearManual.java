/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author T.Chan
 */
public class GearManual implements GearLevel {

    int level;
    
    public GearManual(){
        level = 5;
    }
    
    public GearManual(int level){
        this.level = level;
    }
    
    @Override
    public String getGearLevel() {
        return ""+level;
    }
    
}
