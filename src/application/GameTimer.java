package application;
import sprites.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer {
    private GraphicsContext gc;
	private Scene theScene;
	private WoodSprite woodSprite;
    private SlimeSprite slimeSprite;
    private CandySprite candySprite;
    private IceSprite iceSprite;

    /*
     * TO ADD:
     *  Timers for sprites
     *  Background image for game stage and sprites
     */



     GameTimer(GraphicsContext gc, Scene theScene){
		this.gc = gc;
		this.theScene = theScene;


        /*
         * TO ADD:
         *  Instantiating the timers
         */

        // Instantiate the sprite(s). Can comment out the ones not needed
        this.woodSprite = new WoodSprite("WoodSprite",200,200);
        this.slimeSprite = new SlimeSprite("SlimeSprite",650,750);
        this.candySprite = new CandySprite("CandySprite",690,750);
        this.iceSprite = new IceSprite("IceSprite",730,750);

		//call method to handle mouse click event
		this.handleKeyPressEvent();

     } // end of constructor

     @Override
     public void handle(long currentNanoTime) {
         this.gc.clearRect(0, 0, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
        //  this.gc.drawImage(bg, 0, 0);


        /*
         * TO ADD:
         *  Timer handling for spawning and despawn
         */

        this.woodSprite.move();
        this.slimeSprite.move();
        this.candySprite.move();
        this.iceSprite.move();

         /*
         * TO ADD:
         *  Moving other sprites
         */


         //render the sprite
		this.woodSprite.render(this.gc);
        this.slimeSprite.render(this.gc);
        this.candySprite.render(this.gc);
        this.iceSprite.render(this.gc);

        /*
         * TO ADD:
         *  Rendering other sprites
         */

         // Printing WoodSprite Details
        this.printSpriteDetails();

     } // end of handle method


     	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveMySprite(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopMySprite(code);
		                // mySpriteShoot(code);
		            }
		        });
    }

    //method that will move the sprite depending on the key pressed
	private void moveMySprite(KeyCode ke) {
		if(ke==KeyCode.UP || ke==KeyCode.W) this.woodSprite.setDY(-10);

		if(ke==KeyCode.LEFT || ke==KeyCode.A) this.woodSprite.setDX(-10);

		if(ke==KeyCode.DOWN || ke==KeyCode.S) this.woodSprite.setDY(10);

		if(ke==KeyCode.RIGHT || ke==KeyCode.D) this.woodSprite.setDX(10);

   	}

    //method that will stop the sprite's movement; set the sprite's DX and DY to 0.
	private void stopMySprite(KeyCode ke){
		this.woodSprite.setDX(0);
		this.woodSprite.setDY(0);
		// if(ke==KeyCode.SPACE) this.mySpriteShoot(ke);
	}

    //method to print out the details of the sprite
    private void printSpriteDetails(){
        System.out.println("=============== SPRITE DETAILS ==============");
        System.out.println("Sprite X-coordinate:"+this.woodSprite.getX());
        System.out.println("Sprite Y-coordinate:"+this.woodSprite.getY());
    }
    
}
