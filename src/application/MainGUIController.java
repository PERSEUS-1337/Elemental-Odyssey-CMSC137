package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainGUIController {

    public static final String GAME_NAME = "Elemental Odyssey: Beyond the Horizon";
    // Get the mainThemeMusic.wav file, get its targetPath, and filename
    public static final String MENU_MUSIC = "src\\sounds\\mainThemeMusic.wav";
    public static final Integer WINDOW_WIDTH = 800;
    public static final Integer WINDOW_HEIGHT = 600;

    // media player for the background music
    private static MediaPlayer mediaPlayer;

    @FXML
    private Button btnAbout;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnInstructons;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnSettings;

    @FXML
    private ImageView imgSubTitle;

    @FXML
    private ImageView imgTitle;

    @FXML
    void onAboutButtonClicked(ActionEvent event) {
        System.out.println("About Us Button Clicked");

        // When the about button is clicked, the About Stage will be loaded
        this.loadAboutStage();
    }

    @FXML
    void onExitButtonClicked(ActionEvent event) {
        System.out.println("Exit Button Clicked");
        // When the exit button is clicked, the Exit Stage will be loaded

        this.loadExitStage();
    }

    @FXML
    void onInstructionsButtonClicked(ActionEvent event) {
        System.out.println("Instructions Button Clicked");

        TutorialLevel tutorialLevel = new TutorialLevel();
        tutorialLevel.setStage(new Stage(), MovingBackground.blueColor, TutorialLevel.tutorialWindowSize);

        // if the tutorial level window is closed, play the background music
        Level.getStage().setOnCloseRequest(e -> {
            MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC);
        });

        // Stop the background music if tutorial level window is open
        MainGUIController.stopBackgroundMusic();
    }

    @FXML
    void onPlayButtonClicked(ActionEvent event) {
        System.out.println("Play Button Clicked");
        // When the play button is clicked, the Game Mode Controller will be loaded
        // and the main menu will be closed
        this.loadGameMode();
    }

    @FXML
    void onSettingsButtonClicked(ActionEvent event) {
        System.out.println("Settings Button Clicked");

        // When the settings button is clicked, the Settings Stage will be loaded
        this.loadSettingsStage();
    }

    // Method to load the Game Mode Controller
    private void loadGameMode() {
        try {

            MovingBackground bg = new MovingBackground(MovingBackground.greenColor, MovingBackground.defaultWindowSize);
            // Getting the FXML file for the main menu
            Parent gameModeGuiRoot = FXMLLoader.load(getClass().getResource("/views/GameModeGUI.fxml"));
            // Adding the background and the main menu to the same scene
            Group root = new Group();
            root.getChildren().addAll(bg, gameModeGuiRoot);
            
            Scene scene = new Scene(root, GameModeController.WINDOW_WIDTH, GameModeController.WINDOW_HEIGHT);

            Stage gameModeStage = new Stage();
            gameModeStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            gameModeStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            gameModeStage.setTitle("Select Game Mode");
            gameModeStage.setScene(scene);
            gameModeStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the About Stage
    private void loadAboutStage() {
        try {

            MovingBackground bg = new MovingBackground(MovingBackground.yellowColor, MovingBackground.defaultWindowSize);
            // Getting the FXML file for the main menu
            Parent aboutGuiRoot = FXMLLoader.load(getClass().getResource("/views/AboutStage.fxml"));
            // Adding the background and the main menu to the same scene
            Group root = new Group();
            root.getChildren().addAll(bg, aboutGuiRoot);
            
            Scene scene = new Scene(root, AboutStage.WINDOW_WIDTH, AboutStage.WINDOW_HEIGHT);

            Stage aboutStage = new Stage();
            aboutStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            aboutStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            aboutStage.setTitle("About Us");
            aboutStage.setScene(scene);
            aboutStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the Settings Stage
    private void loadSettingsStage() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/views/SettingsStage.fxml"));
            Scene scene = new Scene(root);

            Stage settingsStage = new Stage();
            settingsStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            settingsStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the Exit Stage
    private void loadExitStage() {
        try {

            MovingBackground bg = new MovingBackground(MovingBackground.grayColor, MovingBackground.defaultWindowSize);
            // Getting the FXML file for the main menu
            Parent exitGuiRoot = FXMLLoader.load(getClass().getResource("/views/ExitStage.fxml"));
            // Adding the background and the main menu to the same scene
            Group root = new Group();
            root.getChildren().addAll(bg, exitGuiRoot);
            
            Scene scene = new Scene(root, ExitStage.WINDOW_WIDTH, ExitStage.WINDOW_HEIGHT);

            Stage exitStage = new Stage();
            exitStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
            exitStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
            exitStage.setTitle("Exit");
            exitStage.setScene(scene);
            exitStage.show();

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to play the main menu music
    public static void playBackgroundMusic(String musicFile) {
        // Creating a new media player with the music file
        Media music = new Media(new File(musicFile).toURI().toString());
        
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Sets the music to loop indefinitely
        mediaPlayer.setVolume(0.5); // Sets the volume to 50%
        mediaPlayer.play(); // Plays the music
        
    }

    // Method to stop the main menu music
    public static void stopBackgroundMusic() {
        mediaPlayer.stop();
    }

}
