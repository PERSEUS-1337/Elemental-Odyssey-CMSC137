package sprites.players;

import javafx.scene.image.Image;
import sprites.Sprite;

public class SlimeSprite extends PlayerSprite{
	public final static Image SPRITE_IMAGE = new Image("assets/players/SlimeSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,false,false);
	public final static String SPRITE_NAME = "SlimeSprite";

	public SlimeSprite(String name, int x, int y){
		super(name, x, y);

		this.loadImage(SlimeSprite.SPRITE_IMAGE);
	}
}