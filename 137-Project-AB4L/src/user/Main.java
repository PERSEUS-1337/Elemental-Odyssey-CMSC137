// This is a Java platformer game where characters can only interact with specific level elements. 
// Players are forced to work together in order to accomplish the puzzles. The concept and levels will be heavily 
// influenced by Fireboy and Watergirl.

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainGUI.fxml"));
            Scene scene = new Scene(root);

            mainStage.setTitle("Harmony of the Elements");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}

}
