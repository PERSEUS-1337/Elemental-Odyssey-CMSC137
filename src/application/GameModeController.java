package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
        // So the chat server will be created first
        ChatGUI chatGUI = new ChatGUI(ChatGUI.CLIENT, "Player Two");
        chatGUI.setStage(new Stage());

        // Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();
    }

    @FXML
    void onCreateGameButtonClicked(ActionEvent event) {
        System.out.println("Create Game Button Clicked");

        // The chat server will be created first
        ChatGUI chatGUI = new ChatGUI(ChatGUI.SERVER, "Player One");
        chatGUI.setStage(new Stage());


        // Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();
    }

}
