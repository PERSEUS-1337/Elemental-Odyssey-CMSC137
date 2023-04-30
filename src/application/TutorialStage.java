package application;
// Import necessary packages
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class TutorialStage {
    public static final int WINDOW_HEIGHT = 800;
	public static final int WINDOW_WIDTH = 1300;
	private Scene scene;
	private static Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;


    // Constructor
	public TutorialStage() {
		this.root = new Group();
		this.scene = new Scene(root, TutorialStage.WINDOW_WIDTH,TutorialStage.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(TutorialStage.WINDOW_WIDTH,TutorialStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene);
	}

    // Method to add the stage elements
	public void setStage(Stage stage) {
		TutorialStage.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);

		TutorialStage.stage.setTitle("Instructions - Tutorial Level");
        TutorialStage.stage.setResizable(false);
        TutorialStage.stage.initModality(Modality.APPLICATION_MODAL);
		TutorialStage.stage.setScene(this.scene);

		//invoke the start method of the animation timer
		this.gametimer.start();
        // After invoking the start method, we need to check if the user exits the window
        // If the user exits the window, we need to stop the timer
        TutorialStage.stage.setOnCloseRequest(e -> {
            this.gametimer.stop();
        });


		TutorialStage.stage.show();
	}

	static Stage getStage(){
		return(TutorialStage.stage);
	}
    
}
