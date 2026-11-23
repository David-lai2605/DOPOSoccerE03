package dominio;


/**
 * Write a description of class Goal here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Goal extends GameObject{
    private boolean isLeftGoal;

    /**
     * Constructor for objects of class Goal
     */
    public Goal(double x, double y, boolean isLeftGoal){
        super(x, y);
        this.isLeftGoal = isLeftGoal;
    }

    @Override
    public void update (double deltaTime) {
        
    }
    
    public boolean checkEntry(Ball ball) {
        if (isLeftGoal) {
            return ball.getPosition().x <= this.position.x && ball.getPosition().y >= 300;
        } else {
            return ball.getPosition().x >= this.position.x && ball.getPosition().y >= 300;
        }
    }
}