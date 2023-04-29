package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainGUIController {

    @FXML
    private Button btnAbout;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnJoin;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnSettings;

    @FXML
    void onAboutButtonClicked(ActionEvent event) {
        System.out.println("About Us Button Clicked");

        // When the about button is clicked, the About Stage will be loaded
        this.loadAboutStage();
    }

    @FXML
    void onExitButtonClicked(ActionEvent event) {
        System.out.println("Exit Button Clicked");
        // When the exit button is clicked, the program will close
        System.exit(0);
    }

    @FXML
    void onJoinButtonClicked(ActionEvent event) {
        System.out.println("Join Button Clicked");
    }

    @FXML
    void onPlayButtonClicked(ActionEvent event) {
        System.out.println("Play Button Clicked");
        // When the play button is clicked, the Game Mode Controller will be loaded
        // and the main menu will be closed
        this.loadGameMode();
    }

    @FXML
    void onSettingsButtonClicked(ActionEvent event) {
        System.out.println("Settings Button Clicked");
    }

    // Method to load the Game Mode Controller
    private void loadGameMode() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/GameModeGUI.fxml"));
            Scene scene = new Scene(root);

            Stage gameModeStage = new Stage();
            gameModeStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            gameModeStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            gameModeStage.setTitle("Select Game Mode");
            gameModeStage.setScene(scene);
            gameModeStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the About Stage
    private void loadAboutStage() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/AboutStage.fxml"));
            Scene scene = new Scene(root);

            Stage aboutStage = new Stage();
            aboutStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            aboutStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            aboutStage.setTitle("About Us");
            aboutStage.setScene(scene);
            aboutStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
