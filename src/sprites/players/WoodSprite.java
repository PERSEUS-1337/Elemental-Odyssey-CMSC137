package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class WoodSprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/players/WoodSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,false,false);
	public final static String SPRITE_NAME = "WoodSprite";

	public WoodSprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(WoodSprite.SPRITE_IMAGE);
	}
}