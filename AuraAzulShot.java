package dominio;


/**
 * Write a description of class AuraAzulShot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AuraAzulShot extends SpecialShot{
    public AuraAzulShot() {
        super(1.5f, 40.0);
    }
    
    @Override
    public void applyEffect(Character target) {
        Vector2 pos = target.getPosition();
        if (pos != null) {
            double nuevoX = pos.x + ((pos.x < 400) ? -80 : 80);
            target.setPosition(nuevoX, pos.y);
        }
    }    
}