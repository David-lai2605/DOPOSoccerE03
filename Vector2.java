package dominio;

import java.io.Serializable;
/**
 * Write a description of class Vector2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Vector2 implements Serializable{
    public double x;
    public double y;

    /**
     * Constructor for objects of class Vector2
     */
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }
}