package dominio;


/**
 * Write a description of class DOPOSoccerException here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DOPOSoccerException extends Exception{
    
    /**
     * Constructor for objects of class DOPOSoccerException
     */
    public DOPOSoccerException(String message){
        super("DOPOSoccer Error: " + message);
    }

}