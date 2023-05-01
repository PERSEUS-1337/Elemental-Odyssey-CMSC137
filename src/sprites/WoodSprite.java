package sprites;

import application.Level;
import javafx.scene.image.Image;



public class WoodSprite extends Sprite{
	private String name;

	public final static Image SPRITE_IMAGE = new Image("assets/WoodSprite_Idle.gif",Sprite.SPRITE_WIDTH,Sprite.SPRITE_WIDTH,false,false);
	public final static int MAX_SPRITE_STRENGTH = 150;
	public final static int MIN_SPRITE_STRENGTH = 100;

	public WoodSprite(String name, int x, int y){
		super(x,y);
		this.name = name;

		this.loadImage(WoodSprite.SPRITE_IMAGE);
	}




	//method called if up/down/left/right arrow key is pressed.
	public void move() {
		
		// Only change the x and y position of the sprite if the current x,y position is within the TutorialStage
        //  width and height so that the sprite won't exit the screen
        if(this.x+this.dx >= 0 && this.x+this.dx <= Level.WINDOW_WIDTH-Sprite.SPRITE_WIDTH &&
            this.y+this.dy >= 0 && this.y+this.dy <= Level.WINDOW_HEIGHT-Sprite.SPRITE_WIDTH){
            this.x += this.dx;
            this.y += this.dy;
        }
		 

	}

	//getters

	String getName(){
		return this.name;
	}

}