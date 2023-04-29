package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

    }

    @FXML
    void onExitButtonClicked(ActionEvent event) {
        // When the exit button is clicked, the program will close
        System.exit(0);
    }

    @FXML
    void onJoinButtonClicked(ActionEvent event) {

    }

    @FXML
    void onPlayButtonClicked(ActionEvent event) {

    }

    @FXML
    void onSettingsButtonClicked(ActionEvent event) {

    }

}
