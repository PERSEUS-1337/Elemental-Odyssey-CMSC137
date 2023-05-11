package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import user.Main;

public class GameOverStage {
    public static final Integer WINDOW_WIDTH = 600;
    public static final Integer WINDOW_HEIGHT = 410;

    @FXML
    private Button btnClose;

    @FXML
    void onCloseButtonClicked(ActionEvent event) {

        // close the GameOverStage
        Level.gameOverStage.close();

        System.out.println("Close Button Clicked");

        // When the close button is clicked, the createMainGUI method will be called
        Main.createMainGUI(Main.mainStage);

        // Play the Main Menu music as well
        MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC, SettingsStage.musicVolume);
    }

}
