package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameOverStage {
    public static final Integer WINDOW_WIDTH = 600;
    public static final Integer WINDOW_HEIGHT = 410;

    @FXML
    private Button btnClose;

    @FXML
    void onCloseButtonClicked(ActionEvent event) {
        System.out.println("Close Button Clicked");
    }

}
