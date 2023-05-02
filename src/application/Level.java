package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sprites.*;
import sprites.objects.*;
import sprites.players.*;

public class Level {
    protected int[][] lvldata;
    protected Scene scene;
	protected static Stage stage;
	protected Group root;
	protected Canvas canvas;
	protected GraphicsContext gc;
	protected GameTimer gametimer;

    public static final int LEVEL_WIDTH = 26;
    public static final int LEVEL_HEIGHT = 15;
    public static final int WINDOW_WIDTH = Sprite.SPRITE_WIDTH * LEVEL_WIDTH;
    public static final int WINDOW_HEIGHT = Sprite.SPRITE_WIDTH * LEVEL_HEIGHT;

    // Constructor
	public Level() {
		this.root = new Group();
		this.scene = new Scene(root, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
	}

    // Method to add the stage elements
	public void setStage(Stage stage) {
		Level.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);

		Level.stage.setTitle("Instructions - Tutorial Level");
        Level.stage.setResizable(false);
        Level.stage.initModality(Modality.APPLICATION_MODAL);
		Level.stage.setScene(this.scene);

        // Build level based on lvldata
        Sprite[][] lvlSprites = new Sprite[LEVEL_HEIGHT][LEVEL_WIDTH];
        for (int i=0; i<Level.LEVEL_HEIGHT; i++){
            for (int j=0; j<Level.LEVEL_WIDTH; j++){
                lvlSprites[i][j] = this.spriteGenerator(lvldata[i][j], j, i);
            }
        }

		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc, this.scene, lvlSprites);

		//invoke the start method of the animation timer
		this.gametimer.start();
        // After invoking the start method, we need to check if the user exits the window
        // If the user exits the window, we need to stop the timer
        Level.stage.setOnCloseRequest(e -> {
            this.gametimer.stop();
        });

		Level.stage.show();
	}

    private Sprite spriteGenerator(int value, int x, int y){
        switch (value) {
            case 1:
                return new WoodSprite("WoodSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 2:
                return new SlimeSprite("SlimeSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 3:
                return new CandySprite("CandySprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 4:
                return new IceSprite("IceSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 5:
                return new TerrainSprite("TerrainSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 6:
                return new CrateSprite("CrateSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 7:
                return new LeverSprite("LeverSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 8:
                return new TerrainSpritePoison("TerrainSpritePoison", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 9:
                return new DoorSprite("DoorSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 10:
                return new ButtonSprite("ButtonSprite", x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            default:
                return null;
        }
    }

	static Stage getStage(){
		return(Level.stage);
	}

}
