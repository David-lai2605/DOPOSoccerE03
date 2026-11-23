package dominio;


/**
 * Write a description of class TornadoShot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TornadoShot extends SpecialShot{
    public TornadoShot() {
        super(3.0f, 40.0);
    }
    
    @Override
    public void applyEffect(Character target) {
        target.stun();
    }
}