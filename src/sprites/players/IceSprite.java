package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class IceSprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/IceSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,true,false);

	public IceSprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(IceSprite.SPRITE_IMAGE);
	}
}