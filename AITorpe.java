package dominio;


/**
 * Write a description of class AITorpe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AITorpe implements IABehaviors{
    private static final long serialVersionUID = 1L;
    private transient float tiempoGiro = 0.0f;
    private transient int direccionActual = 0;
    
    @Override
    public String getNombreComportamiento() {
        return "AITorpe";
    }
    
    @Override
    public void ejecutarComportamiento(float deltaTime, Character jugador, Character rival, Ball balon, int direccionArco) {
        tiempoGiro += deltaTime;
        if (tiempoGiro < 0.65f) {
            tiempoGiro = 0.0f;
            if (Math.random() < 0.35) {
            direccionActual *= -1;
            } else {
                direccionActual = (balon.getPosition().x < jugador.getPosition().x) ? -1 : 1;
            }
        }
        
        double velX = 110.0 * direccionActual;
        jugador.getPosition().x += velX * deltaTime;
        limitarPosicion(jugador);
        
        if (estaCerca(jugador, balon, 45.0)) {
            jugador.kick(rival, balon);
            balon.getVelocity().x = direccionArco * 150.0;
            balon.getVelocity().y = -130.0;
        }
    }
    
    private boolean estaCerca(Character j, Ball b, double radio) {
        return Math.abs(j.getPosition().x - b.getPosition().x) < radio && Math.abs(j.getPosition().y - b.getPosition().y) < radio;
    }
    
    private void limitarPosicion(Character j) {
        if (j.getPosition().x < 60) {
            j.getPosition().x = 60;
        }
        if (j.getPosition().x > 920) {
            j.getPosition().x = 920;
        }
    }
}