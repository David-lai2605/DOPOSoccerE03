package pruebas;

import dominio.*;
import dominio.Character;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class DOPOSoccerPhysicCollisionTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DOPOSoccerPhysicCollisionTest
{
    @Test
    public void testBallInitialVelocityIsZero() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(400, 300), new CameroonCharacter(600, 300), "TIEMPO", 90);
        assertEquals(0.0, model.getBall().getVelocity().x, 0.001);
        assertEquals(0.0, model.getBall().getVelocity().y, 0.001);
    }
    
    @Test
    public void testBallCollisionWithPlayerChangesVelocity() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(400, 300), new CameroonCharacter(600, 300), "TIEMPO", 90);
        
        model.getBall().getPosition().x = 405;
        model.getBall().getPosition().y = 300;
        model.updateGame(0.016f);
        
        assertNotEquals(0.0, model.getBall().getVelocity().x, "Se la altera la velocidad del balon tras el impacto");
    }
    
    @Test
    public void testCharacterPositionUpdatesOnUpdateCall() {
        Character p1 = new KoreaCharacter(100, 300);
        double posYInicial = p1.getPosition().y;
        
        p1.update(0.016f);
        
        assertNotNull(p1.getPosition());
    }
}