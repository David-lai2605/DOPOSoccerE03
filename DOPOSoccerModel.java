package dominio;

import java.io.*;
/**
 * Write a description of class DOPOSoccerModel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DOPOSoccerModel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Character player1;
    private Character player2;
    private Ball ball;
    private Goal leftGoal;
    private Goal rightGoal;
    private String matchType;
    private float matchTimer;
    private int goalLimit;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private String winnerMessage = "";
    
    private boolean player1Auto = false;
    private boolean player2Auto = false;
    private IABehaviors behaviorP1;
    private IABehaviors behaviorP2;

    public DOPOSoccerModel(Character p1, Character p2, String matchType, int limit) {
        this.player1 = p1;
        this.player2 = p2;
        this.matchType = (matchType != null) ? matchType : "TIEMPO";
        this.matchTimer = 90.0f;
        this.goalLimit = limit;

        this.ball = new Ball(500, 350);
        this.leftGoal = new Goal(30, 260, true);
        this.rightGoal = new Goal(920, 260, false);
    }
    
    public void setPlayerConfigurations(boolean p1Auto, IABehaviors behaviorP1, boolean p2Auto, IABehaviors behaviorP2) {
        this.player1Auto = p1Auto;
        this.behaviorP1 = p1Auto ? behaviorP1 : null;
        this.player2Auto = p2Auto;
        this.behaviorP2 = p2Auto ? behaviorP2 : null;
    }
    
    public boolean isPlayer1Auto() {
        return player1Auto;
    }
    
    public boolean isPlayer2Auto() {
        return player2Auto;
    }
    
    public IABehaviors getBehaviorP1() {
        return behaviorP1;
    }
    
    public IABehaviors getBehaviorP2() {
        return behaviorP2;
    }
    
    public void updateGame(float deltaTime) {
        if (isPaused || isGameOver) {
            return;
        }
        
        if ("TIEMPO".equalsIgnoreCase(matchType)) {
            matchTimer -= deltaTime;
            if (matchTimer <= 0.0f) {
                matchTimer = 0.0f;
                checkWinner();
                return;
            }
        }
        
        player1.update(deltaTime);
        player2.update(deltaTime);
        ball.update(deltaTime);
        
        if (player1Auto && behaviorP1 != null){
            behaviorP1.ejecutarComportamiento(deltaTime, player1, player2, ball, 1);
        }
        if (player2Auto && behaviorP2 != null){
            behaviorP2.ejecutarComportamiento(deltaTime, player2, player1, ball, -1);
        }
        
        ball.getVelocity().y += 500.0 * deltaTime;
        
        checkBallCollision(player1);
        checkBallCollision(player2);
        
        if (leftGoal.checkEntry(ball)) {
            registerGoal(player2, player1);
            return;
        } else if (rightGoal.checkEntry(ball)){
            registerGoal(player1, player2);
            return;
        }
        
        double bx = ball.getPosition().x;
        double by = ball.getPosition().y;
        if (bx < 15 || bx > 970 || by < 50 || by > 600) {
            resetPositions();
        }
        
    }
    
    private void checkBallCollision(Character p) {
        double distX = Math.abs(p.getPosition().x - ball.getPosition().x);
        double distY = Math.abs(p.getPosition().y - ball.getPosition().y);
        
        if (distX < 45 && distY < 45) {
            int direccion = (p.getPosition().x < ball.getPosition().x) ? 1: -1;
            ball.getVelocity().x = direccion * 200.0;
            ball.getVelocity().y = -200.0;
        }
    }
    
    private void registerGoal(Character scorer, Character defender) {
        scorer.addGoal();
        defender.getEnergyBar().addQuarter();
        resetPositions();
        
        if ("GOLES".equalsIgnoreCase(matchType) && scorer.getGoal() >= goalLimit) {
            checkWinner();
        }
    }
    
    private void resetPositions() {
        player1.getPosition().x = 380;
        player1.getPosition().y = 350;
        
        player2.getPosition().x = 620;
        player2.getPosition().y = 350;
        
        ball.getPosition().x = 500;
        ball.getPosition().y = 350;
        
        ball.getVelocity().x = 0;
        ball.getVelocity().y = 0;
    }
    
    public void togglePause() {
        this.isPaused = !this.isPaused;
    }
    
    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }
    
    private void checkWinner() {
        this.isGameOver = true;
        if (player1.getGoal() > player2.getGoal()) {
            winnerMessage = "¡Ganador: Jugador 1 (" + player1.getCountry() + ")! +100 puntos";
        } else if (player2.getGoal() > player1.getGoal()) {
            winnerMessage = "¡Ganador: Jugador 2 (" + player2.getCountry() + ")! +100 puntos";
        } else {
            winnerMessage = "Empate";
        }
    }
    
    public void saveGame(String filepath) throws DOPOSoccerException {
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new DOPOSoccerException("Ruta de archivo invalida");
        }
        
        String finalPath = filepath.endsWith(".dopo") ? filepath : filepath + ".dopo";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(finalPath))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new DOPOSoccerException("Error al guardar la partida: " + e.getMessage());
        }
    }
    
    public static DOPOSoccerModel loadGame(String filepath) throws DOPOSoccerException {
        if (filepath == null || filepath.trim().isEmpty()) {
            throw new DOPOSoccerException("Ruta de archivo invalida");
        }
        File file = new File(filepath);
        if(!file.exists() || !file.isFile()) {
            throw new DOPOSoccerException("El archivo de partida no existe");
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (!(obj instanceof DOPOSoccerModel)) {
                throw new DOPOSoccerException("El archivo seleccionado no es una paretida valida de DOPOSoccer");
            }
            return (DOPOSoccerModel) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new DOPOSoccerException("Error al abrir la partida: " + e.getMessage());
        }
    }
    
    public Character getPlayer1() {
        return player1;
    }
    
    public Character getPlayer2() {
        return player2;
    }
    
    public Ball getBall() {
        return ball;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public boolean isGameOver() {
        return isGameOver;
    }
    
    public String getWinnerMessage() {
        return winnerMessage;
    }
    
    public float getMatchTimer() {
        return matchTimer;
    }
}