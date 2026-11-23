package dominio;


/**
 * Write a description of class USACharacter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class USACharacter extends Character{
    public USACharacter(double x, double y) {
        super("Estados Unidos", x, y, new IlusionShot());
    }
}