package dominio;

import java.io.Serializable;
/**
 * Write a description of class EnergyBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnergyBar implements Serializable{
    private float energy = 0.0f;
    
    public void charge(float amount) {
        this.energy = Math.min(100.0f, this.energy + amount);
    }
    
    public void addQuarter() {
        charge(25.0f);
    }
    
    public boolean isFull() {
        return energy >= 100.0f;
    }
    
    public void reset() {
        this.energy = 0.0f;
    }
    
    public float getEnergy() {
        return energy;
    }
} 