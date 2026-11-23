package dominio;

/**
 * Write a description of class IlusionShot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RayoShot extends SpecialShot{
    public RayoShot() {
        super(3.0f, 60.0);
    }
    
    @Override
    public void applyEffect(Character target) {
        target.applyEffectStatus("ELECTROCITADO", 2.5f);
    }
}