package sprites.players;

import application.Level;
import sprites.Sprite;
import sprites.objects.CrateSprite;
import sprites.objects.TerrainSprite;
import java.util.HashMap;
import java.util.Map;

public class PlayerSprite extends Sprite {
    protected String name;
	protected Sprite[][] lvlSprites;
	
	protected boolean inAir = false;

	public final static float GRAVITY = 0.4f * Sprite.SCALE;
	public final static float JUMPSPEED = -7.5f * Sprite.SCALE;
	public final static float FALLSPEEDAFTERCOLLISION = 0.5f * Sprite.SCALE;

    public final static int MOVE_DISTANCE = 3;
    public final static int MAX_SPRITE_STRENGTH = 150;
	public final static int MIN_SPRITE_STRENGTH = 100;

	private int speed = 0;
	private boolean shield = false;

	private int numPowerups = 1;

	private HashMap<PowerUp, Integer> activePowerUps = new HashMap<>();

	// Getters
    public int getSpeed() {
        return speed;
    }

	public boolean getShield() {
		return shield;
	}

	public String getName() {
		return this.name;
	}

	public int getnumPowerups() {
		return this.numPowerups;
	}

	// Setters
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setShield(boolean shield) {
        this.shield = shield;
    }

	public void setnumPowerups() {
		this.numPowerups = 0;
	}

	public PlayerSprite(String name, int x, int y){
		super(x,y);
		this.name = name;
	}

	// Player Sprite methods
	//method called if up/down/left/right arrow key is pressed.
	public void move() {

		// Update inAir if not on floor but haven't jumped
		if (!inAir){
			if (!onFloor()){
				inAir = true;
			}
		}

		if (inAir) {
			// Jump player
			if (this.canMoveHere(this.x, this.y+(int)this.dy)) {
				this.y += this.dy;
				this.dy += GRAVITY;
			} else {
				// Reached floor
				if (this.dy > 0){
					this.y = (int) (this.getCenterY() / this.height) * (int) this.height - 1;
					this.inAir = false;
					this.dy = 0;
				// Reached ceiling
				} else {
					this.y = (int) (this.getCenterY() / this.height) * (int) this.height;
					this.dy = FALLSPEEDAFTERCOLLISION;
				}
			}
		}

		// Check first if sprite can move horizontally
		if (this.canMoveHere(this.x+(int)this.dx, this.y)){
			this.x += this.dx;
		} 
		else {
			// Minimize horizontal gap when colliding
			if (this.dx > 0) this.x = (int) (this.getCenterX() / this.width) * (int) this.width - 1;
			else this.x = (int) (this.getCenterX() / this.width) * (int) this.width;
		}
	}

	// Check if sprite is on the floor
	private boolean onFloor() {
		if (!isSolid(this.x, this.y+(int)this.height))
			if (!isSolid(this.x+(int)this.width, this.y+(int)this.height))
				return false;
		return true;
	}

	// Jump method sets vertical speed
	public void jump(){
		if (!inAir && this.speed!= 4){
			this.inAir = true;
			this.dy = JUMPSPEED;
		} 
	}

	// Check if player can move to destination
	private boolean canMoveHere(int destX, int destY){
		if (!isSolid(destX, destY))
			if (!isSolid(destX+(int)this.width, destY+(int)this.height))
				if (!isSolid(destX+(int)this.width, destY))
					if (!isSolid(destX, destY+(int)this.height))
						return true;

		return false;
	}

	// Check if the sprite in that coordinate is solid (cant move through)
	private boolean isSolid(int targetX, int targetY){
		if (targetX < 0 || targetX >= Level.WINDOW_WIDTH)
			return true;
		if (targetY < 0 || targetY >= Level.WINDOW_HEIGHT)
			return true;

		int xIdx = targetX / Sprite.SPRITE_WIDTH;
		int yIdx = targetY / Sprite.SPRITE_HEIGHT;
		
		Sprite sprite = lvlSprites[yIdx][xIdx];

		if (sprite instanceof TerrainSprite || sprite instanceof CrateSprite){
			return true;
		}

		return false;
	}

	public void setLevelData(Sprite[][] lvlSprites){
		this.lvlSprites = lvlSprites;
	}
	
	/*
	 * Updates the active power-ups by decrementing their durations. Removes expired
	 * power-ups and updates the game state.
	 */
	public void updatePowerUps() {
        HashMap<PowerUp, Integer> updatedPowerUps = new HashMap<>();
        for (Map.Entry<PowerUp, Integer> entry : activePowerUps.entrySet()) {
            PowerUp powerUp = entry.getKey();
            int remainingDuration = entry.getValue() - 1;

            if (remainingDuration > 0) {
                updatedPowerUps.put(powerUp, remainingDuration);
            } else {
                powerUp.deactivate(this);
            }
        }
        activePowerUps = updatedPowerUps;
    }

	public void addActivePowerUp(PowerUp powerUp, int duration) {
        activePowerUps.put(powerUp, duration);
    }
}
