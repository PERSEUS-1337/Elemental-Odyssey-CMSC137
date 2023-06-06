package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ExitStage {
    public static final Integer WINDOW_WIDTH = 538;
    public static final Integer WINDOW_HEIGHT = 235;

    @FXML
    private Button btnNo;

    @FXML
    private Button btnYes;

    @FXML
    void onNoButtonClicked(ActionEvent event) {
        // When the no button is clicked, the Exit Window will be hidden
        System.out.println("No Button Clicked");
        btnNo.getScene().getWindow().hide();
    }

    @FXML
    void onYesButtonClicked(ActionEvent event) {
        // When the yes button is clicked, the program will exit
        System.out.println("Yes Button Clicked");
        System.exit(0);
    }

}
