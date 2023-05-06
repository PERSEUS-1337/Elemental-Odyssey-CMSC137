package sprites.players;

import application.Level;
import sprites.Sprite;
import sprites.objects.CrateSprite;
import sprites.objects.TerrainSprite;

public class PlayerSprite extends Sprite {
    protected String name;
	protected Sprite[][] lvlSprites;

    public final static int MOVE_DISTANCE = 2;
    public final static int MAX_SPRITE_STRENGTH = 150;
	public final static int MIN_SPRITE_STRENGTH = 100;

	public PlayerSprite(String name, int x, int y) {
		super(x, y);
		this.name = name;
	}

	// method called if up/down/left/right arrow key is pressed.
	public void move() {

		// Check first if sprite can move horizontally
		if (this.canMoveX(this.x+this.dx)){
			this.x += this.dx;
		}

		// Check first if sprite can move vertically
		if (this.canMoveY(this.y+this.dy)){
			this.y += this.dy;
		}
	}

	// Check if player can move horizontally to destination
	private boolean canMoveX(int destX){
		if (!isSolid(destX, this.y))
			if (!isSolid(destX+(int)this.width, this.y+(int)this.height))
				if (!isSolid(destX+(int)this.width, this.y))
					if (!isSolid(destX, this.y+(int)this.height))
						return true;

		return false;
	}

	// Check if player can move vertically to destination
	private boolean canMoveY(int destY){
		if (!isSolid(this.x, destY))
			if (!isSolid(this.x+(int)this.width, destY+(int)this.height))
				if (!isSolid(this.x+(int)this.width, destY))
					if (!isSolid(this.x, destY+(int)this.height))
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
	
	public String getName(){
		return this.name;
	}

}
