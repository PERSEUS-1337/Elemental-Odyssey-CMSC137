package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class IceSprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/players/IceSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,false,false);
	public final static String SPRITE_NAME = "IceSprite";

	public IceSprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(IceSprite.SPRITE_IMAGE);
	}
}