package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import user.Main;

public class GameOverStage {
    public static final Integer WINDOW_WIDTH = 600;
    public static final Integer WINDOW_HEIGHT = 410;

    // Music and sounds stuff
    private static MediaPlayer mediaPlayer;
    public static String GAME_OVER_MUSIC = setGameOverMusicPath();

    @FXML
    private Button btnClose;

    @FXML
    void onCloseButtonClicked(ActionEvent event) {

        // close the GameOverStage
        Level.gameOverStage.close();

        System.out.println("Close Button Clicked");

        // When the close button is clicked, the createMainGUI method will be called
        Main.createMainGUI(Main.mainStage);

        // Stop the game over music
        GameOverStage.stopBackgroundMusic();

        // Play the Main Menu music as well
        MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC, SettingsStage.musicVolume);
    }

    // Method to play the gameover music
    public static void playBackgroundMusic(String musicFile, double volume) {
        // Creating a new media player with the music file
        Media music = new Media(new File(musicFile).toURI().toString());
        
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Sets the music to loop indefinitely
        mediaPlayer.setVolume(volume); // Sets the volume of the music
        mediaPlayer.play(); // Plays the music
        
    }

    // Method to stop the main menu music
    public static void stopBackgroundMusic() {
        mediaPlayer.stop();
    }

    // Method to change the volume of the music
    public static void changeMusicVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    // Method to set the game over music path
    private static String setGameOverMusicPath(){
        String path = "";

        try {
            path = "src/sounds/gameOverThemeMusic.wav";
        } catch (Exception e) {
        } finally {
            path = "src\\sounds\\gameOverThemeMusic.wav";
        }
        return path;
    }

}
