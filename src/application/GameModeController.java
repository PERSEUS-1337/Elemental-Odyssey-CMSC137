package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameModeController {

    @FXML
    private Button btnMultiPlayer;

    @FXML
    private Button btnSinglePlayer;

    @FXML
    void onMultiPlayerButtonClicked(ActionEvent event) {
        System.out.println("Multi Player Button Clicked");
    }

    @FXML
    void onSinglePlayerButtonClicked(ActionEvent event) {
        System.out.println("Single Player Button Clicked");
    }

}
