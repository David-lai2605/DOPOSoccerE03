package dominio;


/**
 * Write a description of class AITorpe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AIOfensiva implements IABehaviors{
    private static final long serialVersionUID = 1L;
    private transient float cooldownPatada = 0.0f;
    
    @Override
    public String getNombreComportamiento() {
        return "AIOfensiva";
    }
    
    @Override
    public void ejecutarComportamiento(float deltaTime, Character jugador, Character rival, Ball balon, int direccionArco) {
       cooldownPatada = Math.max(0.0f, cooldownPatada - deltaTime);
       
       double objetivoX = balon.getPosition().x + (direccionArco * 20.0);
       double diferencia = objetivoX - jugador.getPosition().x;
       if (Math.abs(diferencia) > 6.0) {
           double dir = Math.signum(diferencia);
           jugador.getPosition().x += dir * 230.0 * deltaTime;
       }
       
       limitarPosicion(jugador);
       
       if (cooldownPatada <= 0.0f && estaCerca(jugador, balon, 60.0)) {
           jugador.kick(rival, balon);
           balon.getVelocity().x = direccionArco * 330.0;
           balon.getVelocity().y = -210.0;
           cooldownPatada = 0.25f;
       }
       
       if (jugador.getEnergyBar().getEnergy() >= 50.0f && !jugador.hasPowerActive()) {
           jugador.activatePower();
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