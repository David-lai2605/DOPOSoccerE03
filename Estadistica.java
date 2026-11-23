package dominio;

import java.io.Serializable;
/**
 * Write a description of class Estadistica here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Estadistica
{
    private int speedLevel = 1;
    private int jumpLevel = 1;
    private int kickLevel = 1;
    private int survivalLevel = 1;

    public double getKickMultiplier() {
        return 1.0 + 0.10 * (kickLevel - 1);
    }
    
    public double getMaxHealth() {
        return 100.0 * (1.0 + 0.10 * (survivalLevel - 1));
    }
    
    public void upgradeKick() throws DOPOSoccerException {
        if (kickLevel < 10) {
            kickLevel++;
        } else {
            throw new DOPOSoccerException("Kick ya esta en el nivel maximo");
        }
    }
    
    public void upgradeSurvival() throws DOPOSoccerException {
        if (survivalLevel < 10) {
            survivalLevel++;
        } else {
            throw new DOPOSoccerException("Survival ya esta en el nivel maximo");
        }
    }
    
    public int getKickLevel() {
        return kickLevel;
    }
    
    public int getSurvivalLevel() {
        return survivalLevel;
    }
}