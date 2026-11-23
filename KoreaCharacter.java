package dominio;


/**
 * Write a description of class KoreaCharacter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KoreaCharacter extends Character{
    public KoreaCharacter(double x, double y) {
        super("Corea del Sur", x, y, new AuraAzulShot());
    }
}