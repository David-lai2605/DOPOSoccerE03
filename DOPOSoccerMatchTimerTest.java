package pruebas;

import dominio.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class DOPOSoccerMatchTimerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class DOPOSoccerMatchTimerTest
{
    @Test
    public void testInitialTimerValue() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(100, 300), new CameroonCharacter(700, 300), "TIEMPO", 90);
        assertEquals(90.0f, model.getMatchTimer(), 0.001);
    }
    
    @Test
    public void testTimerDecreasesWithDeltaTime() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(100, 300), new CameroonCharacter(700, 300), "TIEMPO", 90);
        
        float tiempoAntes = model.getMatchTimer();
        model.updateGame(2.5f);
        
        assertTrue(model.getMatchTimer() < tiempoAntes, " El temporizador debe reducirse con el transcurso del tiempo");
    }
    
    @Test
    public void testTimerDoesNotDecreaseWhenPaused() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(100, 300), new CameroonCharacter(700, 300), "TIEMPO", 90);
        
        model.togglePause();
        float tiempoAntes = model.getMatchTimer();
        model.updateGame(3.0f);
        
        assertEquals(tiempoAntes, model.getMatchTimer(), 0.001, "El temporizador no avanza si el juego esta en pausa");
    }
    
    @Test
    public void testGmaeOverTriggerredWhenTimerExpires() {
        DOPOSoccerModel model = new DOPOSoccerModel(new KoreaCharacter(100, 300), new CameroonCharacter(700, 300), "TIEMPO", 90);
        
        assertFalse(model.isGameOver());
        model.updateGame(91.0f);
        assertTrue(model.isGameOver(), "El jeugo termina despues de 90 segundos");
    }
}