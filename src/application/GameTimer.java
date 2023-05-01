package application;
import sprites.*;
import sprites.players.*;
import sprites.objects.WallSprite;
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
    private Sprite[][] lvlSprites;

    /*
     * TO ADD:
     *  Timers for sprites
     *  Background image for game stage and sprites
     */



     GameTimer(GraphicsContext gc, Scene theScene, Sprite[][] lvlSprites){
		this.gc = gc;
		this.theScene = theScene;
        this.lvlSprites = lvlSprites;

        /*
         * TO ADD:
         *  Instantiating the timers
         */
        
        // Get variable reference to player sprites
        for (int i=0; i<Level.LEVEL_HEIGHT; i++){
            for (int j=0; j<Level.LEVEL_WIDTH; j++){
                if (lvlSprites[i][j] instanceof WoodSprite) 
                    this.woodSprite = (WoodSprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof SlimeSprite) 
                    this.slimeSprite = (SlimeSprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof CandySprite) 
                    this.candySprite = (CandySprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof IceSprite) 
                    this.iceSprite = (IceSprite) lvlSprites[i][j];
            }
        }

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


        //render the sprites
        for (int i=0; i<Level.LEVEL_HEIGHT; i++){
            for (int j=0; j<Level.LEVEL_WIDTH; j++){
                if (lvlSprites[i][j] != null)
                    lvlSprites[i][j].render(this.gc);
            }
        }

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
		if(ke==KeyCode.W) this.woodSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.A) this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.S) this.woodSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.D) this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);

        if(ke==KeyCode.T) this.slimeSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.F) this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.G) this.slimeSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.H) this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);

        if(ke==KeyCode.I) this.candySprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.J) this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.K) this.candySprite.setDY(PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.L) this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);

        if(ke==KeyCode.UP) this.iceSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
        if(ke==KeyCode.LEFT) this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.DOWN) this.iceSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		if(ke==KeyCode.RIGHT) this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);

   	}

    //method that will stop the sprite's movement; set the sprite's DX and DY to 0.
	private void stopMySprite(KeyCode ke){
		this.woodSprite.setDX(0);
		this.woodSprite.setDY(0);
        this.slimeSprite.setDX(0);
        this.slimeSprite.setDY(0);
        this.candySprite.setDX(0);
        this.candySprite.setDY(0);
        this.iceSprite.setDX(0);
        this.iceSprite.setDY(0);
		// if(ke==KeyCode.SPACE) this.mySpriteShoot(ke);
	}

    //method to print out the details of the sprite
    private void printSpriteDetails(){
        System.out.println("=============== SPRITE DETAILS ==============");
        System.out.println("Sprite X-coordinate:"+this.woodSprite.getX());
        System.out.println("Sprite Y-coordinate:"+this.woodSprite.getY());
    }
    
}
