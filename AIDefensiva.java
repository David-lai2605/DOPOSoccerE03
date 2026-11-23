package dominio;


/**
 * Write a description of class AITorpe here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AIDefensiva implements IABehaviors{
    private static final long serialVersionUID = 1L;
    private transient float cooldownPatada = 0.0f;
    
    @Override
    public String getNombreComportamiento() {
        return "AIDefensiva";
    }
    
    @Override
    public void ejecutarComportamiento(float deltaTime, Character jugador, Character rival, Ball balon, int direccionArco) {
       cooldownPatada = Math.max(0.0f, cooldownPatada - deltaTime);
       
       double zonaBaseX = (direccionArco == 1) ? 240.0 : 740.0;
       double objetivoX = Math.abs(balon.getPosition().x - zonaBaseX) < 240.0 ? balon.getPosition().x : zonaBaseX;
       double diferencia = objetivoX - jugador.getPosition().x;
       
       if (Math.abs(diferencia) > 8.0) {
           double dir = Math.signum(diferencia);
           jugador.getPosition().x += dir * 170.0 * deltaTime;
       }
       
       limitarPosicion(jugador);
       
       if (cooldownPatada <= 0.0f && estaCerca(jugador, balon, 55.0)) {
           jugador.kick(rival, balon);
           balon.getVelocity().x = direccionArco * 260.0;
           balon.getVelocity().y = -180.0;
           cooldownPatada = 0.35f;
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