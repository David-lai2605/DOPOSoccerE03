package dominio;

import java.io.Serializable;
/**
 * Write a description of class GameObject here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class GameObject implements Serializable{
    protected Vector2 position;
    protected Vector2 velocity;
    
    /**
     * Constructor for objects of class GameObject
     */
    public GameObject(double x, double y){
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
    }

    public abstract void update(double delatTime);
    
    public Vector2 getPosition() {
        return position;
    }
    
    public Vector2 getVelocity() {
        return velocity;
    }
    
    public void setPosition(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }
}