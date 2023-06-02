package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sprites.*;
import sprites.objects.*;
import sprites.players.*;
import user.Main;

public class Level {
    protected int[][] lvldata;
    protected Scene scene;
	protected static Stage stage;
    protected static Stage gameOverStage;
	protected Group root;
	protected Canvas canvas;
	protected GraphicsContext gc;
	protected GameTimer gametimer;
    protected Boolean isMultiplayer;
    protected String chatType;
    protected String nameOfUser;
    protected String ipAddress;
    protected String spriteType;
    protected ChatGUI chat;

    public static final int LEVEL_WIDTH = 40;
    public static final int LEVEL_HEIGHT = 23;
    public static final int WINDOW_WIDTH = Sprite.SPRITE_WIDTH * LEVEL_WIDTH;
    public static final int WINDOW_HEIGHT = Sprite.SPRITE_WIDTH * LEVEL_HEIGHT;

    // Music and sounds stuff
    public static final String TRACK_01 = setGameMusicPath();
    // media player for the background music
    private static MediaPlayer mediaPlayer;

    // Constructor
	public Level(Boolean isMultiplayer, String chatType, String nameOfUser, String ipAddress, String spriteType, ChatGUI chat) {
        this.chatType = chatType;
        this.nameOfUser = nameOfUser;
        this.ipAddress = ipAddress;
        this.isMultiplayer = isMultiplayer;
        this.spriteType = spriteType;
        this.chat = chat;
        
        System.out.println("isMultiplayer: " + isMultiplayer);
		this.root = new Group();
		this.scene = new Scene(root, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
	}

    // Method to add the stage elements
	public void setStage(Stage stage, String backgroundColor, Integer windowSize) {
        if(!this.isMultiplayer){
            Level.stage = stage;

            // Setup the moving background image
            MovingBackground movingBackground = new MovingBackground(backgroundColor, windowSize);
    
            //set stage elements here
            this.root.getChildren().addAll(movingBackground,canvas);
    
            Level.stage.setTitle("Instructions - Tutorial Level");
            Level.stage.setResizable(false);
            Level.stage.initModality(Modality.APPLICATION_MODAL);
            Level.stage.setScene(this.scene);
    
            // Build level based on lvldata
            Sprite[][] lvlSprites = new Sprite[LEVEL_HEIGHT][LEVEL_WIDTH];
            for (int i=0; i<Level.LEVEL_HEIGHT; i++){
                for (int j=0; j<Level.LEVEL_WIDTH; j++){
                    lvlSprites[i][j] = this.spriteGenerator(lvldata[i][j], j, i);
                }
            }
    
            //instantiate an animation timer
            this.gametimer = new GameTimer(this.gc, this.scene, lvlSprites, isMultiplayer, null, null, null, this.spriteType, null);
    
            // play the background music
            playBackgroundMusic(TRACK_01, SettingsStage.masterVolume);

            //invoke the start method of the animation timer
            this.gametimer.start();
            // After invoking the start method, we need to check if the user exits the window
            // If the user exits the window, we need to stop the timer
            Level.stage.setOnCloseRequest(e -> {
                this.gametimer.stop();
            });
    
            Level.stage.show();
        } else {
            Level.stage = stage;

            // Setup the moving background image
            MovingBackground movingBackground = new MovingBackground(backgroundColor, windowSize);
    
            //set stage elements here
            this.root.getChildren().addAll(movingBackground,canvas);
    
            Level.stage.setTitle("Instructions - Tutorial Level");
            Level.stage.setResizable(false);
            Level.stage.initModality(Modality.APPLICATION_MODAL);
            Level.stage.setScene(this.scene);
    
            // Build level based on lvldata
            Sprite[][] lvlSprites = new Sprite[LEVEL_HEIGHT][LEVEL_WIDTH];
            for (int i=0; i<Level.LEVEL_HEIGHT; i++){
                for (int j=0; j<Level.LEVEL_WIDTH; j++){
                    lvlSprites[i][j] = this.spriteGenerator(lvldata[i][j], j, i);
                }
            }
    
            //instantiate an animation timer
            this.gametimer = new GameTimer(this.gc, this.scene, lvlSprites, this.isMultiplayer, this.chatType, this.nameOfUser, this.ipAddress, this.spriteType, this.chat);
    
            // play the background music
            playBackgroundMusic(TRACK_01, SettingsStage.masterVolume);

            //invoke the start method of the animation timer
            this.gametimer.start();
            // After invoking the start method, we need to check if the user exits the window
            // If the user exits the window, we need to stop the timer
            Level.stage.setOnCloseRequest(e -> {
                this.gametimer.stop();
            });
    
            Level.stage.show();
        }
	}

    // Method to generate the sprites
    private Sprite spriteGenerator(int value, int x, int y){
        switch (value) {
            case 1:
                return new WoodSprite(WoodSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH-1);
            case 2:
                return new SlimeSprite(SlimeSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH-1);
            case 3:
                return new CandySprite(CandySprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH-1);
            case 4:
                return new IceSprite(IceSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH-1);
            case 5:
                return new TerrainSprite(TerrainSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 6:
                return new CrateSprite(CrateSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 7:
                return new LeverSprite(LeverSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 8:
                return new TerrainSpritePoison(TerrainSpritePoison.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 9:
                return new DoorSprite(DoorSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            case 10:
                return new ButtonSprite(ButtonSprite.SPRITE_NAME, x*Sprite.SPRITE_WIDTH, y*Sprite.SPRITE_WIDTH);
            default:
                return null;
        }
    }

    // Method to get the stage
	static Stage getStage(){
		return(Level.stage);
	}

    // Method to stop the timer    
	public void stopTimer(){
		this.gametimer.stop();
	}

    // Method to play the main menu music
    public static void playBackgroundMusic(String musicFile, double volume) {
        // Creating a new media player with the music file
        Media music = new Media(new File(musicFile).toURI().toString());
        
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Sets the music to loop indefinitely
        mediaPlayer.setVolume(volume); // Sets the volume to 50%
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

    // Method to set the game over stage
    public static void setGameOver(ArrayList<String> rankings, HashMap<String, String> rankingArray, Boolean isMultiplayer){
    PauseTransition transition = new PauseTransition(Duration.seconds(1));
    transition.play();

    transition.setOnFinished(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent arg0) {
            if(isMultiplayer){
                // Must show the gameOver screen
                try {
                    LeaderBoardStage leaderBoard = new LeaderBoardStage(rankings, rankingArray);
                    
                    MovingBackground bg = new MovingBackground(MovingBackground.yellowColor, MovingBackground.defaultWindowSize);
                    // Getting the FXML file for the about ui
                    Parent gameOverRoot = FXMLLoader.load(getClass().getResource("/views/GameOverStage.fxml"));
                    // Adding the background and the about ui to the same scene
                    Group root = new Group();
                    root.getChildren().addAll(bg, gameOverRoot, leaderBoard);
                    
                    Scene scene = new Scene(root, GameOverStage.WINDOW_WIDTH, GameOverStage.WINDOW_HEIGHT);
        
                    gameOverStage = new Stage();
                    gameOverStage.initModality(Modality.APPLICATION_MODAL); // Prevents user from interacting with other windows
                    gameOverStage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
                    gameOverStage.setTitle("Gameover!");
                    gameOverStage.setScene(scene);

                    // Close the music
                    mediaPlayer.stop();

                    // Close the current level stage
                    Level.getStage().close();

                    gameOverStage.show();

                    // Play the background music
                    try {
                    GameOverStage.playBackgroundMusic(GameOverStage.GAME_OVER_MUSIC, SettingsStage.masterVolume);
                    } catch (Exception e) {
                    System.out.println("Error playing music: " + e.getMessage());
                    }
        
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Close the music
                mediaPlayer.stop();
                
                // Close the current level stage
                Level.getStage().close();

                // When the close button is clicked, the createMainGUI method will be called
                Main.createMainGUI(Main.mainStage);
                // Play the Main Menu music as well
                MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC, SettingsStage.musicVolume);
            }
        }
    });
}

    // Method to close the game over stage
    public static void closeGameOverStage(){
        gameOverStage.close();
    }
    
    // Method to set the music path
    private static String setGameMusicPath(){
        String musicPath = "";
        try {
            musicPath = "src/sounds/musicTrack01.wav";
        } catch (Exception e) {
        } finally {
            musicPath = "src\\sounds\\musicTrack01.wav";
        }
        return musicPath;
    }
}
