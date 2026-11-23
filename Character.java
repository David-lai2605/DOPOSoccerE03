package dominio;


/**
 * Write a description of class Character here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Character extends GameObject{
    protected String country;
    protected int goals = 0;
    protected double currentHealth;
    protected boolean isStunned = false;
    protected float stunTimer = 0.0f;
    protected EnergyBar energyBar;
    protected SpecialShot specialShot;
    protected Estadistica stats;
    protected String activeEffect = null;
    protected float effectTimer = 0.0f;
    protected boolean hasPowerActive = false;
    
    public Character (String country, double x, double y, SpecialShot shot) {
        super(x, y);
        this.country = country;
        this.stats = new Estadistica();
        this.currentHealth = stats.getMaxHealth();
        this.energyBar = new EnergyBar();
        this.specialShot = shot;
    }
    
    @Override
    public void update(double deltaTime) {
        if (!hasPowerActive && !energyBar.isFull()) {
            energyBar.charge((float) (10.0 * deltaTime));
        }
        
        if (isStunned) {
            stunTimer -= deltaTime;
            if (stunTimer <= 0) {
                isStunned = false;
                currentHealth = stats.getMaxHealth();
            }
        }
        
        if (activeEffect != null) {
            effectTimer -= deltaTime;
            if (effectTimer <= 0) {
                activeEffect = null;
            }
        }
        
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }
    
    public void kick(Character opponent, Ball ball) {
        if (Math.abs(this.position.x - opponent.getPosition().x) < 55) {
            double damage = 10.0 * stats.getKickMultiplier();
            opponent.receiveDamage(damage);
        }
        
        double distBallX = Math.abs(this.position.x - ball.getPosition().x);
        double distBallY = Math.abs(this.position.y - ball.getPosition().y);
        
        if (distBallX < 45 && distBallY < 45) {
            int direccion = (this.position.x < ball.getPosition().x) ? 1 : -1;
            
            ball.getVelocity().x = direccion * 350.0;
            ball.getVelocity().y = -300.0;
        }
    }
    
    public void activatePower() {
        if (energyBar.isFull() && !hasPowerActive && !isStunned) {
            hasPowerActive = true;
            energyBar.reset();
        }
    }
    
    public void executeSpecial(Ball ball, Character opponent) {
        if (!hasPowerActive) {
            return;
        }
        
        double totalDamage = specialShot.getBaseDamage() * stats.getKickMultiplier();
        opponent.receiveDamage(totalDamage);
        specialShot.applyEffect(opponent);
        hasPowerActive = false;
    }
    
    public void receiveDamage(double amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            currentHealth = 0;
            stun();
        }
    }
    
    public void stun() {
        this.isStunned = true;
        this.stunTimer = 3.0f;
    }
    
    public void applyEffectStatus(String effect, float duration) {
        this.activeEffect = effect;
        this.effectTimer = duration;
    }
    
    public Vector2 getPosition() {
        return position;
    }
    
    public void setPosition (double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }
    
    public int getGoal() {
        return goals;
    }
    
    public void addGoal() {
        goals++;
    }
    
    public double getCurrentHealth() {
        return currentHealth;
    }
    
    public EnergyBar getEnergyBar() {
        return energyBar;
    }
    
    public boolean hasPowerActive() {
        return hasPowerActive;
    }
    
    public String getCountry() {
        return country;
    }
    
    public Estadistica getStats() {
        return stats;
    }
}