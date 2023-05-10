package application;

import sprites.*;
import sprites.objects.DoorSprite;
import sprites.players.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    private DoorSprite doorSprite;  // endpoint indicator
    private Sprite[][] lvlSprites;
    private ArrayList<KeyCode> pressed;

    // GameOver-related variables
    private boolean gameOver;
    private ArrayList<String> playerRanking;
    // We need to keep track of the time from the start of the game to the end of the game
    private long startTime;
    private long endTime;
    private int doorIndexX;
    private int doorIndexY;

    public static final int FPS = 60;

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

        // Initialize GameOver-related variables
        this.gameOver = false;
        this.playerRanking = new ArrayList<String>();
        this.startTime = System.nanoTime();
        this.endTime = 0;

        // Get the index of the door based on the lvldata
        this.locateDoor();
        System.out.println("Door index: " + this.doorIndexY + ", " + this.doorIndexX);
        this.doorSprite = (DoorSprite) lvlSprites[this.doorIndexY][this.doorIndexX];

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

        // give reference of lvlSprites to players to interact with surroundings
        this.woodSprite.setLevelData(lvlSprites);
        this.slimeSprite.setLevelData(lvlSprites);
        this.candySprite.setLevelData(lvlSprites);
        this.iceSprite.setLevelData(lvlSprites);

        // call method to handle mouse click event
        this.handleKeyPressEvent();

    } // end of constructor

     @Override
     public void handle(long currentNanoTime) {
         this.gc.clearRect(0, 0, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
        
        // Update the time
        long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
		long gameStartSec = TimeUnit.NANOSECONDS.toSeconds(this.startTime);
		int passedTime = (int) (currentSec - gameStartSec);

        System.out.println("Passed Time: " + passedTime + " seconds");
        

        // Move the sprites
        moveMySprite();
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

        // After the sprites have been rendered, check if the player sprites have reached the end of the level, which is colliding with the Door sprite
        this.checkDoorCollision();

        // Put thread to sleep until next frame
        try {
            Thread.sleep((1000 / GameTimer.FPS) - ((System.nanoTime() - currentNanoTime) / 1000000));
        } catch (Exception e) {}

    } // end of handle method

    // method that will listen and handle the key press events
    private void handleKeyPressEvent() {
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
            	if (!pressed.contains(code))
            		pressed.add(code);
            }
        });

        this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
            	KeyCode code = e.getCode();
            	pressed.remove(code);
            }
        });
    }

    //method that will move the sprite depending on the key pressed
	private void moveMySprite() {
		// Wood Sprite movement
        if (pressed.contains(KeyCode.W)) this.woodSprite.jump();
		if (pressed.contains(KeyCode.A) && pressed.contains(KeyCode.D)) this.woodSprite.setDX(0);
		else if (pressed.contains(KeyCode.A)) this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.D)) this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.woodSprite.setDX(0);

		// Slime Sprite movement
        if (pressed.contains(KeyCode.T)) this.slimeSprite.jump();
		if (pressed.contains(KeyCode.F) && pressed.contains(KeyCode.H)) this.slimeSprite.setDX(0);
		else if (pressed.contains(KeyCode.F)) this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.H)) this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.slimeSprite.setDX(0);

        // Candy Sprite movement
		if (pressed.contains(KeyCode.I)) this.candySprite.jump();
		if (pressed.contains(KeyCode.J) && pressed.contains(KeyCode.L)) this.candySprite.setDX(0);
		else if (pressed.contains(KeyCode.J)) this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
		else if (pressed.contains(KeyCode.L)) this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
		else this.candySprite.setDX(0);

        // Ice Sprite movement
        if (pressed.contains(KeyCode.UP)) this.iceSprite.jump();
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
        // int xCoord = this.woodSprite.getCenterX();
        // int yCoord = this.woodSprite.getCenterY();
        // int xIndex = xCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        // int yIndex = yCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);
        // System.out.println("========================");
        // System.out.println("Sprite X-coordinate:" + xCoord);
        // System.out.println("Sprite Y-coordinate:" + yCoord);
        // System.out.println("Sprite X-index:" + xIndex);
        // System.out.println("Sprite Y-index:" + yIndex);
        // System.out.println("Sprite Collides with:" + this.lvlSprites[yIndex][xIndex]);
        // System.out.println("Collission? " + this.woodSprite.collidesWith(this.lvlSprites[yIndex][xIndex]));

    }

    // method to locate the door sprite in the level data. It updates the doorIndexY and doorIndexX
    private void locateDoor() {
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if (this.lvlSprites[i][j] instanceof DoorSprite) {
                    this.doorIndexY = i;
                    this.doorIndexX = j;
                }
            }
        }
    }

    // method to check if the sprite is colliding with the door. If it is, add the sprite to the player ranking
    private void checkDoorCollision() {
        if (this.woodSprite.collidesWith(this.doorSprite) == true && !this.playerRanking.contains("WoodSprite")) {
            this.playerRanking.add("WoodSprite");
        }
        else if (this.slimeSprite.collidesWith(this.doorSprite) == true && !this.playerRanking.contains("SlimeSprite")) {
            this.playerRanking.add("SlimeSprite");
        }
        else if (this.candySprite.collidesWith(this.doorSprite) == true && !this.playerRanking.contains("CandySprite")) {
            this.playerRanking.add("CandySprite");
        }
        else if (this.iceSprite.collidesWith(this.doorSprite) == true && !this.playerRanking.contains("IceSprite")) {
            this.playerRanking.add("IceSprite");
        }
    }
}
