package dominio;

import java.io.Serializable;
/**
 * Write a description of interface IABehaviors here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public interface IABehaviors extends Serializable{
    String getNombreComportamiento();
    void ejecutarComportamiento(float deltaTime, Character jugador, Character rival, Ball balon, int direccionArco);
}