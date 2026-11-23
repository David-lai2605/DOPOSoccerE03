package pruebas;

import dominio.*;
import dominio.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class DOPOSoccerSpecialShotTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DOPOSoccerSpecialShotTest
{
    @Test
    public void testEnergyBarInitialStateZero() {
        Character p1 = new NigeriaCharacter(100, 300);
        assertEquals(0.0f, p1.getEnergyBar().getEnergy(), 0.001);
    }
    
    @Test
    public void testEnergyBarPartialCharging() {
        Character p1 = new NigeriaCharacter(100, 300);
        p1.getEnergyBar().addQuarter();
        assertEquals(25.0f, p1.getEnergyBar().getEnergy(), 0.001);
    }
    
    
    @Test
    public void testSpecialPowerCannotActivateWithoutFullEnergy() {
        Character p1 = new NigeriaCharacter(100, 300);
        p1.getEnergyBar().addQuarter();
        
        p1.activatePower();
        assertFalse(p1.hasPowerActive(), "El poder no se activa al menos que la barra de energia este llena");
    }
    
    @Test
    public void testSpecialPowerActivateWhenEnergyIsFull() {
        Character p1 = new NigeriaCharacter(100, 300);
        p1.getEnergyBar().charge(100.0f);
        
        assertTrue(p1.getEnergyBar().isFull());
        p1.activatePower();
        assertTrue(p1.hasPowerActive(), "El poder debe acrivarse con la barra de energia llena");
    }
}