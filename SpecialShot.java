package dominio;

import java.io.Serializable;
/**
 * Write a description of class SpecialShot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class SpecialShot implements Serializable{
    protected float duration;
    protected double baseDamage;
    
    public SpecialShot (float duration, double baseDamage) {
        this.duration = duration;
        this.baseDamage = baseDamage;
    }
    
    public abstract void applyEffect (Character target) ;
    
    public double getBaseDamage() {
        return baseDamage;
    }
    
    public float getDuration() {
        return duration;
    }
}