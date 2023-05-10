package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class CandySprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/players/CandySprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,false,false);
	public final static String SPRITE_NAME = "CandySprite";

	public CandySprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(CandySprite.SPRITE_IMAGE);
	}
}