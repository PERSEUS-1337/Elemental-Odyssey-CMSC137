// This is a Java platformer game where characters can only interact with specific level elements. 
// Players are forced to work together in order to accomplish the puzzles. The concept and levels will be heavily 
// influenced by Fireboy and Watergirl.

package user;

import java.io.IOException;

import application.MainGUIController;
import application.MovingBackground;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage mainStage){
        // Get the FXML file for the main menu from the 'views' folder
        try {
            // Adding the background to the main menu
            MovingBackground bg = new MovingBackground(MovingBackground.brownColor, MovingBackground.defaultWindowSize);
            // Getting the FXML file for the main menu
            Parent mainGuiRoot = FXMLLoader.load(getClass().getResource("/views/MainGUI.fxml"));
            // Adding the background and the main menu to the same scene
            Group root = new Group();
            root.getChildren().addAll(bg, mainGuiRoot);
            
            Scene scene = new Scene(root, MainGUIController.WINDOW_WIDTH, MainGUIController.WINDOW_HEIGHT);

            mainStage.setResizable(false); // Disables the ability to resize the window
            mainStage.setTitle(MainGUIController.GAME_NAME);
            mainStage.setScene(scene);
            mainStage.show();

            // Play the background music
           try {
            MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC);
           } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}

}
