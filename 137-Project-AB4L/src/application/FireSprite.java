package application;

import javafx.scene.image.Image;



public class FireSprite extends Sprite{
	private String name;

	public final static Image SHIP_IMAGE = new Image("images/fireboy.png",FireSprite.SHIP_WIDTH*0.70,FireSprite.SHIP_WIDTH,false,false);
	public final static int SHIP_WIDTH = 100;
	public final static int MAX_SHIP_STRENGTH = 150;
	public final static int MIN_SHIP_STRENGTH = 100;

	public FireSprite(String name, int x, int y){
		super(x,y);
		this.name = name;

		this.loadImage(FireSprite.SHIP_IMAGE);
	}


    

	//method called if up/down/left/right arrow key is pressed.
	void move() {
		
		// Only change the x and y position of the sprite if the current x,y position is within the TutorialStage
        //  width and height so that the sprite won't exit the screen
        if(this.x+this.dx > 0 && this.x+this.dx <= TutorialStage.WINDOW_WIDTH-80 &&
            this.y+this.dy > 0 && this.y+this.dy <= TutorialStage.WINDOW_HEIGHT-100){
            this.x += this.dx;
            this.y += this.dy;
        }
		 

	}

	//getters

	String getName(){
		return this.name;
	}

}
