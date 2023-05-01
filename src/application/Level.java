package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Level {
    protected int[][] tilegrid;
    protected Scene scene;
	protected static Stage stage;
	protected Group root;
	protected Canvas canvas;
	protected GraphicsContext gc;
	protected GameTimer gametimer;

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static int LEVEL_WIDTH = 64;
    public static int LEVEL_HEIGHT = 48;

    

    // Constructor
	public Level() {
		this.root = new Group();
		this.scene = new Scene(root, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene);
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

		//invoke the start method of the animation timer
		this.gametimer.start();
        // After invoking the start method, we need to check if the user exits the window
        // If the user exits the window, we need to stop the timer
        Level.stage.setOnCloseRequest(e -> {
            this.gametimer.stop();
        });


		Level.stage.show();
	}

	static Stage getStage(){
		return(Level.stage);
	}
}
