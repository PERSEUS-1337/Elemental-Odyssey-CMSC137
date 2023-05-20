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
        // So the chat server will be created first
        System.out.println("Join Game Button Clicked");
        ChatGUI chatGUI = new ChatGUI(ChatGUI.CLIENT, "Client");
        chatGUI.setStage(new Stage());

        // Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();

        // If the chat gui has been closed, close its socket
        chatGUI.getStage().setOnCloseRequest(e -> {
            chatGUI.closeSocket();
        });
    }

    @FXML
    void onCreateGameButtonClicked(ActionEvent event) {
        System.out.println("Create Game Button Clicked");
        ChatGUI chatGUI = new ChatGUI(ChatGUI.SERVER, "Server");

        // The chat server will be created first
        chatGUI.setStage(new Stage());

        // Close the Game Mode Menu
        MainGUIController.closeGameModeMenu();

        // If the chat gui has been closed, close its socket
        chatGUI.getStage().setOnCloseRequest(e -> {
            chatGUI.closeSocket();
        });
    }

}
