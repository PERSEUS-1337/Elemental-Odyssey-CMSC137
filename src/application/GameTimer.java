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

    public static final int FPS = 30;

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

        // give reference of lvlSprites to players to interact with surroundings
        this.woodSprite.setLevelData(lvlSprites);
        this.slimeSprite.setLevelData(lvlSprites);
        this.candySprite.setLevelData(lvlSprites);
        this.iceSprite.setLevelData(lvlSprites);

        // call method to handle mouse click event
        this.handleKeyPressEvent();

    } // end of constructor

    // private long lastTime = 0;
    // private long fpsCap = 60;
    // private final long targetTime = 1000000000L / fpsCap;

    @Override
    public void handle(long currentNanoTime) {
        // if (!(currentNanoTime - lastTime >= targetTime)) {
        // return;
        // }
        // lastTime = currentNanoTime;

        this.gc.clearRect(0, 0, Level.WINDOW_WIDTH, Level.WINDOW_HEIGHT);

        // Draw background
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                this.gc.drawImage(Level.BACKGROUND, j * Sprite.SPRITE_WIDTH, i * Sprite.SPRITE_WIDTH);
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
        /*
         * GRAVITY LOGIC PART 1
         * 
         * Implemented in game timer so gravity is always active no matter when you
         * press
         * 
         * Capped to inverse of jump_speed, so there is terminal velocity.
         * 
         * And this still moves player with the cap
         * 
         * TODO: No Gravity Reset yet when hitting a platform
         */
        if (PlayerSprite.getVERTICAL_VELOCITY() < (-this.woodSprite.getJUMP_SPEED()))
            // Need to implement proper setter here
            PlayerSprite.VERTICAL_VELOCITY += this.woodSprite.getGravity();
        this.woodSprite.setDY(PlayerSprite.getVERTICAL_VELOCITY());

        // collision detection (should be improved in the future)
        // this.collissionDetection();

        // Printing WoodSprite Details
        this.printSpriteDetails();

        // Put thread to sleep until next frame
        try {
            Thread.sleep((1000 / GameTimer.FPS) - ((System.nanoTime() - currentNanoTime) / 1000000));
        } catch (Exception e) {
        }

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
            // public void handle(KeyEvent e) {
            // KeyCode code = e.getCode();
            // if (code == KeyCode.W && !wPressed) {
            // wPressed = true;
            // pressed.add(code);
            // moveMySprite();
            // } else if (code != KeyCode.W) {
            // if (!pressed.contains(code)) {
            // pressed.add(code);
            // moveMySprite();
            // }
            // }
            // }
        });

        this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (pressed.contains(code))
                    pressed.remove(code);
                moveMySprite();
            }
            // public void handle(KeyEvent e) {
            // KeyCode code = e.getCode();
            // if (code == KeyCode.W) {
            // wPressed = false;
            // }
            // if (pressed.contains(code)) {
            // pressed.remove(code);
            // moveMySprite();
            // }
            // }
        });
    }

    // method that will move the sprite depending on the key pressed
    private void moveMySprite() {
        // Vertical movement (Wood Sprite)
        if (pressed.contains(KeyCode.W) && pressed.contains(KeyCode.S))
            this.woodSprite.setDY(0);
        else if (pressed.contains(KeyCode.W)) {
            // When jumping, set jump speed
            PlayerSprite.setVERTICAL_VELOCITY(this.woodSprite.getJUMP_SPEED());
            // Make player move to jump speed
            this.woodSprite.setDY(-PlayerSprite.getVERTICAL_VELOCITY());
        } else if (pressed.contains(KeyCode.S)) {
            // this.woodSprite.setDY(PlayerSprite.MOVE_DISTANCE);
            // PlayerSprite.VERTICAL_VELOCITY -= 2;
            // this.woodSprite.setDY(PlayerSprite.VERTICAL_VELOCITY);
        } else {
            // PlayerSprite.VERTICAL_VELOCITY -= 2;
            // this.woodSprite.setDY(PlayerSprite.VERTICAL_VELOCITY);
        }

        // Horizontal movement (Wood Sprite)
        if (pressed.contains(KeyCode.A) && pressed.contains(KeyCode.D))
            this.woodSprite.setDX(0);
        else if (pressed.contains(KeyCode.A))
            this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.D))
            this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
        else
            this.woodSprite.setDX(0);

        // if (PlayerSprite.VERTICAL_VELOCITY <= 8)
        // PlayerSprite.VERTICAL_VELOCITY += 1;
        // this.woodSprite.setDY(PlayerSprite.VERTICAL_VELOCITY);

        // Vertical movement (Slime Sprite)
        if (pressed.contains(KeyCode.T) && pressed.contains(KeyCode.G))
            this.slimeSprite.setDY(0);
        else if (pressed.contains(KeyCode.T))
            this.slimeSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.G))
            this.slimeSprite.setDY(PlayerSprite.MOVE_DISTANCE);
        else
            this.slimeSprite.setDY(0);
        // Horizontal movement (Slime Sprite)
        if (pressed.contains(KeyCode.F) && pressed.contains(KeyCode.H))
            this.slimeSprite.setDX(0);
        else if (pressed.contains(KeyCode.F))
            this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.H))
            this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
        else
            this.slimeSprite.setDX(0);

        // Vertical movement (Candy Sprite)
        if (pressed.contains(KeyCode.I) && pressed.contains(KeyCode.K))
            this.candySprite.setDY(0);
        else if (pressed.contains(KeyCode.I))
            this.candySprite.setDY(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.K))
            this.candySprite.setDY(PlayerSprite.MOVE_DISTANCE);
        else
            this.candySprite.setDY(0);
        // Horizontal movement (Candy Sprite)
        if (pressed.contains(KeyCode.J) && pressed.contains(KeyCode.L))
            this.candySprite.setDX(0);
        else if (pressed.contains(KeyCode.J))
            this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.L))
            this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
        else
            this.candySprite.setDX(0);

        // Vertical movement (Ice Sprite)
        if (pressed.contains(KeyCode.UP) && pressed.contains(KeyCode.DOWN))
            this.iceSprite.setDY(0);
        else if (pressed.contains(KeyCode.UP))
            this.iceSprite.setDY(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.DOWN))
            this.iceSprite.setDY(PlayerSprite.MOVE_DISTANCE);
        else
            this.iceSprite.setDY(0);
        // Horizontal movement (Ice Sprite)
        if (pressed.contains(KeyCode.LEFT) && pressed.contains(KeyCode.RIGHT))
            this.iceSprite.setDX(0);
        else if (pressed.contains(KeyCode.LEFT))
            this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
        else if (pressed.contains(KeyCode.RIGHT))
            this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
        else
            this.iceSprite.setDX(0);

        // // Current Player (Multiplayer)
        // // Vertical movement
        // if (pressed.contains(KeyCode.UP) && pressed.contains(KeyCode.DOWN))
        // this.mySprite.setDY(0);
        // else if (pressed.contains(KeyCode.UP))
        // this.mySprite.setDY(-PlayerSprite.MOVE_DISTANCE);
        // else if (pressed.contains(KeyCode.DOWN))
        // this.mySprite.setDY(PlayerSprite.MOVE_DISTANCE);
        // else this.mySprite.setDY(0);
        // // Horizontal movement
        // if (pressed.contains(KeyCode.LEFT) && pressed.contains(KeyCode.RIGHT))
        // this.mySprite.setDX(0);
        // else if (pressed.contains(KeyCode.LEFT))
        // this.mySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
        // else if (pressed.contains(KeyCode.RIGHT))
        // this.mySprite.setDX(PlayerSprite.MOVE_DISTANCE);
        // else this.mySprite.setDX(0);
    }

    // method to print out the details of the sprite
    private void printSpriteDetails() {

        // System.out.println("=============== SPRITE DETAILS ==============");
        System.out.println("Velocity: " + (int) PlayerSprite.getVERTICAL_VELOCITY());
        // int xCoord = this.woodSprite.getCenterX();
        // int yCoord = this.woodSprite.getCenterY();
        // int xIndex = xCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        // int yIndex = yCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);
        // System.out.println("========================");
        // System.out.println("Sprite X-coordinate:" + xCoord);
        // System.out.println("Sprite Y-coordinate:" + yCoord);
        // System.out.println("Sprite X-index:" + xIndex);
        // System.out.println("Sprite Y-index:" + yIndex);
        // System.out.println("Sprite Collides with:" +
        // this.lvlSprites[yIndex][xIndex]);
        // System.out.println("Collission? " +
        // this.woodSprite.collidesWith(this.lvlSprites[yIndex][xIndex]));

    }
}
