package pruebas;

import dominio.KoreaCharacter;
import dominio.CameroonCharacter;
import dominio.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {
    @Test
    public void testCargaBarraDePoderAlRecibirGol() {
        Character p1 = new KoreaCharacter(100, 400);
        
        assertEquals(0.0f, p1.getEnergyBar().getEnergy(), 0.001);
        
        p1.getEnergyBar().addQuarter();
        assertEquals(25.0f, p1.getEnergyBar().getEnergy(), 0.001);
    }
    
    @Test
    public void testRecibirDanioYAturdamiento() {
        Character p2 = new CameroonCharacter(700, 400);
        double vidaMax = p2.getStats().getMaxHealth();
        
        assertEquals(vidaMax, p2.getCurrentHealth(), 0.001);
        
        p2.receiveDamage(vidaMax + 10.0);
        assertEquals(0.0, p2.getCurrentHealth(), 0.001);
    }
}