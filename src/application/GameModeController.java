package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameModeController {
    public static final Integer WINDOW_WIDTH = 538;
    public static final Integer WINDOW_HEIGHT = 235;
    private Stage enterAddressStage;

    @FXML
    private Button btnMultiPlayer;

    @FXML
    private Button btnSinglePlayer;

    @FXML
    void onJoinGameButtonClicked(ActionEvent event) {
        // Create the Enter Address Stage
        EnterAddressStage enterAddressStage = new EnterAddressStage(ChatGUI.CLIENT);
        enterAddressStage.setStage(new Stage());
    }

    @FXML
    void onCreateGameButtonClicked(ActionEvent event) {
        System.out.println("Create Game Button Clicked");

        // Create the Enter Address Stage
        EnterNameStage enterNameStage = new EnterNameStage(ChatGUI.DEFAULT_SERVER, ChatGUI.SERVER);
            enterNameStage.setStage(new Stage());

        
    }

    Stage getEnterAddressStage() {
        return this.enterAddressStage;
    }

}
