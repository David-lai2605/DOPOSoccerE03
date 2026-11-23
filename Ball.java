package dominio;


/**
 * Write a description of class Ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ball extends GameObject{
    private float radius = 15.0f;
    private boolean isFake = false;

    /**
     * Constructor for objects of class Ball
     */
    public Ball(double x, double y){
        super(x, y);
    }

    @Override
    public void update(double deltaTime) {
        velocity.y += 9.8 *10 * deltaTime;
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
        
        if (position.y >= 450) {
            position.y = 450;
            velocity.y = -velocity.y * 0.7;
        }
    }
    
    public float getRadius() {
        return radius;
    }
    
    public boolean isFake() {
        return isFake;
    }
    
    public void setFake(boolean fake) {
        isFake = fake;
    }
}