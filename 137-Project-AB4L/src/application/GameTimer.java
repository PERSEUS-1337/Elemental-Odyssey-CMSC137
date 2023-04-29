package application;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer {
    private GraphicsContext gc;
	private Scene theScene;
	private FireSprite fireBoy;

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

        this.fireBoy = new FireSprite("FireBoy",190,670);

		//call method to handle mouse click event
		this.handleKeyPressEvent();

     } // end of constructor

     @Override
     public void handle(long currentNanoTime) {
         this.gc.clearRect(0, 0, TutorialStage.WINDOW_WIDTH,TutorialStage.WINDOW_HEIGHT);
        //  this.gc.drawImage(bg, 0, 0);


        /*
         * TO ADD:
         *  Timer handling for spawning and despawn
         */

         this.fireBoy.move();

         /*
         * TO ADD:
         *  Moving other sprites
         */


         //render the sprite
		this.fireBoy.render(this.gc);

        /*
         * TO ADD:
         *  Rendering other sprites
         */

         // Printing FireSprite Details
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
		if(ke==KeyCode.UP || ke==KeyCode.W) this.fireBoy.setDY(-10);

		if(ke==KeyCode.LEFT || ke==KeyCode.A) this.fireBoy.setDX(-10);

		if(ke==KeyCode.DOWN || ke==KeyCode.S) this.fireBoy.setDY(10);

		if(ke==KeyCode.RIGHT || ke==KeyCode.D) this.fireBoy.setDX(10);

   	}

    //method that will stop the sprite's movement; set the sprite's DX and DY to 0.
	private void stopMySprite(KeyCode ke){
		this.fireBoy.setDX(0);
		this.fireBoy.setDY(0);
		// if(ke==KeyCode.SPACE) this.mySpriteShoot(ke);
	}

    //method to print out the details of the sprite
    private void printSpriteDetails(){
        System.out.println("=============== SPRITE DETAILS ==============");
        System.out.println("FireSprite X-coordinate:"+this.fireBoy.getX());
        System.out.println("FireSprite Y-coordinate:"+this.fireBoy.getY());
    }
    
}
