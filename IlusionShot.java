package dominio;

import java.io.Serializable;
/**
 * Write a description of class IlusionShot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class IlusionShot extends SpecialShot{
    public IlusionShot() {
        super(2.5f, 30.0);
    }
    
    @Override
    public void applyEffect(Character target) {
        target.applyEffectStatus("CONFUSION", 2.5f);
    }
}