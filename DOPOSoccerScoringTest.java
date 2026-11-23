package pruebas;

import dominio.*;
import dominio.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class DOPOSoccerScoringTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DOPOSoccerScoringTest
{
    @Test
    public void testInitialScoresAreZero() {
        Character p1 = new KoreaCharacter(180, 420);
        Character p2 = new CameroonCharacter(670, 420);
        assertEquals(0, p1.getGoal());
        assertEquals(0, p2.getGoal());
    }
    
    @Test
    public void testGoalIncrementsScoreAndResetsPositions() {
        Character p1 = new KoreaCharacter(180, 420);
        Character p2 = new CameroonCharacter(670, 420);
        DOPOSoccerModel model = new DOPOSoccerModel(p1, p2, "TIEMPO", 90);
        
        model.getBall().getPosition().x = 830;
        model.getBall().getPosition().y = 390;
        model.updateGame(0.016f);
        
        assertEquals(1, p1.getGoal(), "El jugador 1 deberia registrar un gol");
    
    }
    
    @Test
    public void testDefenderReceivesEnergyBoostOnGoal() {
        Character p1 = new KoreaCharacter(180, 420);
        Character p2 = new CameroonCharacter(670, 420);
        DOPOSoccerModel model = new DOPOSoccerModel(p1, p2, "TIEMPO", 90);
        
        float energiaInicialP2 = p2.getEnergyBar().getEnergy();
        
        model.getBall().getPosition().x = 830;
        model.getBall().getPosition().y = 390;
        model.updateGame(0.016f);
        
        assertTrue(p2.getEnergyBar().getEnergy() > energiaInicialP2, "El defensor recibe energia al tapar el gol");
    }
    
    @Test
    public void testGoalLimitModeTriggerGameOver() {
        Character p1 = new KoreaCharacter(180, 420);
        Character p2 = new CameroonCharacter(670, 420);
        DOPOSoccerModel model = new DOPOSoccerModel(p1, p2, "GOLES", 1);
        
        assertFalse(model.isGameOver());
        
        model.getBall().getPosition().x = 830;
        model.getBall().getPosition().y = 390;
        model.updateGame(0.016f);
        
        assertTrue(model.isGameOver(), "Debe alcanzar el limite de goles");
    }
}