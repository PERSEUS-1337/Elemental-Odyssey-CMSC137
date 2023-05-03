package application;

import sprites.*;
import sprites.players.*;

import java.util.ArrayList;
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
    private ArrayList<KeyCode> pressed;

    /*
     * TO ADD:
     * Timers for sprites
     * Background image for game stage and sprites
     */

    GameTimer(GraphicsContext gc, Scene theScene, Sprite[][] lvlSprites) {
        this.gc = gc;
        this.theScene = theScene;
        this.lvlSprites = lvlSprites;
        this.pressed = new ArrayList<KeyCode>();

        /*
         * TO ADD:
         * Instantiating the timers
         */

        // Get variable reference to player sprites
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
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

        // call method to handle mouse click event
        this.handleKeyPressEvent();

    } // end of constructor

     @Override
     public void handle(long currentNanoTime) {
         this.gc.clearRect(0, 0, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
        
        // Draw background
        for (int i=0; i<Level.LEVEL_HEIGHT; i++){
            for (int j=0; j<Level.LEVEL_WIDTH; j++){
                this.gc.drawImage(Level.BACKGROUND, j*Sprite.SPRITE_WIDTH, i*Sprite.SPRITE_WIDTH);
            }
        }

        /*
         * TO ADD:
         * Timer handling for spawning and despawn
         */

        this.woodSprite.move();
        this.slimeSprite.move();
        this.candySprite.move();
        this.iceSprite.move();

        /*
         * TO ADD:
         * Moving other sprites
         */

        // render the sprites
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if (lvlSprites[i][j] != null)
                    lvlSprites[i][j].render(this.gc);
            }
        }

        // Printing WoodSprite Details
        this.printSpriteDetails();

    } // end of handle method

    // method that will listen and handle the key press events
    private void handleKeyPressEvent() {
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
            	if (!pressed.contains(code))
            		pressed.add(code);
                moveMySprite();
            }
        });

        this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
            	KeyCode code = e.getCode();
            	if (pressed.contains(code))
            		pressed.remove(code);
                moveMySprite();
            }
        });
    }

    //method that will move the sprite depending on the key pressed
	private void moveMySprite() {
		// Vertical movement (Wood Sprite)
		if (pressed.contains(KeyCode.W) && pressed.contains(KeyCode.S)) this.woodSprite.setDY(0);
		else if (pressed.contains(KeyCode.W)) this.woodSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.S)) this.woodSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		else this.woodSprite.setDY(0);
		// Horizontal movement (Wood Sprite)
		if (pressed.contains(KeyCode.A) && pressed.contains(KeyCode.D)) this.woodSprite.setDX(0);
		else if (pressed.contains(KeyCode.A)) this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.D)) this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.woodSprite.setDX(0);

        // Vertical movement (Slime Sprite)
		if (pressed.contains(KeyCode.T) && pressed.contains(KeyCode.G)) this.slimeSprite.setDY(0);
		else if (pressed.contains(KeyCode.T)) this.slimeSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.G)) this.slimeSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		else this.slimeSprite.setDY(0);
		// Horizontal movement (Slime Sprite)
		if (pressed.contains(KeyCode.F) && pressed.contains(KeyCode.H)) this.slimeSprite.setDX(0);
		else if (pressed.contains(KeyCode.F)) this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.H)) this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.slimeSprite.setDX(0);

        // Vertical movement (Candy Sprite)
		if (pressed.contains(KeyCode.I) && pressed.contains(KeyCode.K)) this.candySprite.setDY(0);
		else if (pressed.contains(KeyCode.I)) this.candySprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.K)) this.candySprite.setDY(PlayerSprite.MOVE_DISTANCE);
		else this.candySprite.setDY(0);
		// Horizontal movement (Candy Sprite)
		if (pressed.contains(KeyCode.J) && pressed.contains(KeyCode.L)) this.candySprite.setDX(0);
		else if (pressed.contains(KeyCode.J)) this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.L)) this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.candySprite.setDX(0);

        // Vertical movement (Ice Sprite)
		if (pressed.contains(KeyCode.UP) && pressed.contains(KeyCode.DOWN)) this.iceSprite.setDY(0);
		else if (pressed.contains(KeyCode.UP)) this.iceSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.DOWN)) this.iceSprite.setDY(PlayerSprite.MOVE_DISTANCE);
		else this.iceSprite.setDY(0);
		// Horizontal movement (Ice Sprite)
		if (pressed.contains(KeyCode.LEFT) && pressed.contains(KeyCode.RIGHT)) this.iceSprite.setDX(0);
		else if (pressed.contains(KeyCode.LEFT)) this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.RIGHT)) this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.iceSprite.setDX(0);

        // // Current Player (Multiplayer)
        // // Vertical movement
		// if (pressed.contains(KeyCode.UP) && pressed.contains(KeyCode.DOWN)) this.mySprite.setDY(0);
		// else if (pressed.contains(KeyCode.UP)) this.mySprite.setDY(-PlayerSprite.MOVE_DISTANCE);
		// else if (pressed.contains(KeyCode.DOWN)) this.mySprite.setDY(PlayerSprite.MOVE_DISTANCE);
		// else this.mySprite.setDY(0);
		// // Horizontal movement
		// if (pressed.contains(KeyCode.LEFT) && pressed.contains(KeyCode.RIGHT)) this.mySprite.setDX(0);
		// else if (pressed.contains(KeyCode.LEFT)) this.mySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		// else if (pressed.contains(KeyCode.RIGHT)) this.mySprite.setDX(PlayerSprite.MOVE_DISTANCE);
		// else this.mySprite.setDX(0);
   	}

    // method to print out the details of the sprite
    private void printSpriteDetails() {
        // System.out.println("=============== SPRITE DETAILS ==============");
        int xCoord = this.woodSprite.getX();
        int yCoord = this.woodSprite.getY();
        int xIndex = xCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        int yIndex = yCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT) + 1;
        System.out.println("========================");
        System.out.println("Sprite X-coordinate:" + xCoord);
        System.out.println("Sprite Y-coordinate:" + yCoord);
        System.out.println("Sprite X-index:" + xIndex);
        System.out.println("Sprite Y-index:" + yIndex);
        System.out.println("Sprite Collides with:" + this.lvlSprites[yIndex][xIndex]);
        System.out.println("Collission? " + this.woodSprite.collidesWith(this.lvlSprites[yIndex][xIndex]));

    }

}
