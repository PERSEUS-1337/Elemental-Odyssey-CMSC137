package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class WoodSprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/WoodSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,true,false);

	public WoodSprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(WoodSprite.SPRITE_IMAGE);
	}
}