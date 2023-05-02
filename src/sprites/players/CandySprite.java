package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class CandySprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/CandySprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,true,false);

	public CandySprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(CandySprite.SPRITE_IMAGE);
	}
}