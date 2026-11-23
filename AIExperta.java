package dominio;


/**
 * Write a description of class AITorpe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AIExperta implements IABehaviors{
    private static final long serialVersionUID = 1L;
    private transient float cooldownPatada = 0.0f;
    
    @Override
    public String getNombreComportamiento() {
        return "AIExperta";
    }
    
    @Override
    public void ejecutarComportamiento(float deltaTime, Character jugador, Character rival, Ball balon, int direccionArco) {
       cooldownPatada = Math.max(0.0f, cooldownPatada - deltaTime);
       
       double diferencia = balon.getPosition().x - jugador.getPosition().x;
       if (Math.abs(diferencia) > 5.0) {
           double dir = Math.signum(diferencia);
           jugador.getPosition().x += dir * 260.0 * deltaTime;
       }
       
       limitarPosicion(jugador);
       
       if (cooldownPatada <= 0.0f && estaCerca(jugador, balon, 62.0)) {
           jugador.kick(rival, balon);
           balon.getVelocity().x = direccionArco * 360.0;
           balon.getVelocity().y = -240.0;
           cooldownPatada = 0.22f;
       }
       
       if (jugador.getEnergyBar().getEnergy() >= 40.0f && !jugador.hasPowerActive()) {
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