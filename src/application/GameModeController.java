package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameModeController {
    public static final Integer WINDOW_WIDTH = 538;
    public static final Integer WINDOW_HEIGHT = 235;
    // private Integer numConnectedClients = 1;


    @FXML
    private Button btnMultiPlayer;

    @FXML
    private Button btnSinglePlayer;

    // private String generateChatterName() {
    //     String chatterName = "Player " + (numConnectedClients + 1);
    //     numConnectedClients++;
    //     return chatterName;
    // }
    

    @FXML
    void onJoinGameButtonClicked(ActionEvent event) {
        // So the chat server will be created first
        System.out.println("Join Game Button Clicked");
        // String chatterName = generateChatterName();
        ChatGUI chatGUI = new ChatGUI(ChatGUI.CLIENT);
        // numConnectedClients++;
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
        // String chatterName = generateChatterName();
        ChatGUI chatGUI = new ChatGUI(ChatGUI.SERVER);
        // numConnectedClients++;

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
