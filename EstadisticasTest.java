package pruebas;

import dominio.Estadistica;
import dominio.DOPOSoccerException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EstadisticasTest {
    @Test
    public void testValoresInicialesEstadisticas() {
        Estadistica stats = new Estadistica();
        assertEquals(100.0, stats.getMaxHealth(), 0.001);
        assertEquals(1.0, stats.getKickMultiplier(), 0.001);
    }
    
    @Test
    public void testMejorasSurvivalYVidaMax() throws DOPOSoccerException {
        Estadistica stats = new Estadistica();
        
        stats.upgradeSurvival();
        assertEquals(110.0, stats.getMaxHealth(), 0.001);
    }
    
    @Test
    public void testMultiplicadorKick() {
        Estadistica stats = new Estadistica();
        
        assertEquals(1.0, stats.getKickMultiplier(), 0.001);
    }
}