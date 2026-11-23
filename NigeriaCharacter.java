package dominio;


/**
 * Write a description of class USACharacter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NigeriaCharacter extends Character{
    public NigeriaCharacter(double x, double y) {
        super("Nigeria", x, y, new TornadoShot());
    }
}