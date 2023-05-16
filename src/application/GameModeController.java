package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameModeController {
    public static final Integer WINDOW_WIDTH = 538;
    public static final Integer WINDOW_HEIGHT = 235;

    @FXML
    private Button btnMultiPlayer;

    @FXML
    private Button btnSinglePlayer;

    @FXML
    void onJoinGameButtonClicked(ActionEvent event) {
        System.out.println("Join Game Button Clicked");
    }

    @FXML
    void onCreateGameButtonClicked(ActionEvent event) {
        System.out.println("Create Game Button Clicked");
    }

}
