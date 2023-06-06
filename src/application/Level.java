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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public static final int BUTTON_X = WINDOW_WIDTH - 80;
    public static final int BUTTON_Y = 15;

    // Instructional Stage stuff
    public static final int TUTORIAL_LEVEL_HEADER_X = 350;
    public static final int TUTORIAL_LEVEL_HEADER_Y = -50;
    public static final int TUTORIAL_LEVEL_HEADER_WIDTH = 590;
    public static final int TUTORIAL_LEVEL_HEADER_HEIGHT = 270;
    // Move Text
    public static final int MOVE_TEXT_X = 150;
    public static final int MOVE_TEXT_Y = 300;
    public static final int MOVE_TEXT_WIDTH = 160;
    public static final int MOVE_TEXT_HEIGHT = 120;
    // Left-Right Key Gif
    public static final int LEFT_RIGHT_KEY_X = 150;
    public static final int LEFT_RIGHT_KEY_Y = 380;
    public static final int LEFT_RIGHT_KEY_WIDTH = 160;
    public static final int LEFT_RIGHT_KEY_HEIGHT = 120;
    // Jump Text
    public static final int JUMP_TEXT_X = 580;
    public static final int JUMP_TEXT_Y = 300;
    public static final int JUMP_TEXT_WIDTH = 160;
    public static final int JUMP_TEXT_HEIGHT = 120;
    // Jump Key Gif
    public static final int JUMP_KEY_GIF_X = 620;
    public static final int JUMP_KEY_GIF_Y = 380;
    public static final int JUMP_KEY_GIF_WIDTH = 77;
    public static final int JUMP_KEY_GIF_HEIGHT = 117;
    // Goal Text
    public static final int GOAL_TEXT_X = 1000;
    public static final int GOAL_TEXT_Y = 300;
    public static final int GOAL_TEXT_WIDTH = 160;
    public static final int GOAL_TEXT_HEIGHT = 120;
    // Goal Arrow Png
    public static final int GOAL_ARROW_PNG_X = 1020;
    public static final int GOAL_ARROW_PNG_Y = 380;
    public static final int GOAL_ARROW_PNG_WIDTH = 150;
    public static final int GOAL_ARROW_PNG_HEIGHT = 200;
    // Start Sign Png
    public static final int START_FLAG_PNG_X = 19;
    public static final int START_FLAG_PNG_Y = 674;
    public static final int START_FLAG_PNG_WIDTH = 60;
    public static final int START_FLAG_PNG_HEIGHT = 30;

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
            Level.stage = stage;

            // Setup the moving background image
            MovingBackground movingBackground = new MovingBackground(backgroundColor, windowSize);

            // Add the exit button
            Button exitButton = new Button();
            exitButton.setLayoutX(BUTTON_X);
            exitButton.setLayoutY(BUTTON_Y);
            exitButton.setPrefSize(25, 20);
            // Set the image of the exit button
            exitButton.setStyle("-fx-background-image: url('/assets/buttons/CloseButton.png'); -fx-background-size: 100% 100%;");
            exitButton.setOnAction(this.goBackToMainMenu());
    
            //set stage elements here
            this.root.getChildren().addAll(movingBackground,canvas, exitButton);
    
            Level.stage.setTitle(MainGUIController.GAME_NAME);
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
    
            if(!isMultiplayer){

                // for the singleplayer (or tutorial level), we include the instructional assets

                // Level Header Title
                ImageView tutorialHeader = new ImageView(new Image(getClass().getResourceAsStream("../assets/texts/tutorialText.png")));
                tutorialHeader.setFitWidth(TUTORIAL_LEVEL_HEADER_WIDTH);
                tutorialHeader.setFitHeight(TUTORIAL_LEVEL_HEADER_HEIGHT);
                tutorialHeader.setX(TUTORIAL_LEVEL_HEADER_X);
                tutorialHeader.setY(TUTORIAL_LEVEL_HEADER_Y);

                // Move Text
                ImageView moveText = new ImageView(new Image(getClass().getResourceAsStream("../assets/texts/moveText.png")));
                moveText.setFitWidth(MOVE_TEXT_WIDTH);
                moveText.setFitHeight(MOVE_TEXT_HEIGHT);
                moveText.setX(MOVE_TEXT_X);
                moveText.setY(MOVE_TEXT_Y);

                // Left-Right Key Gif
                ImageView leftRightKey = new ImageView(new Image(getClass().getResourceAsStream("../assets/objects/leftRightKeys.gif")));
                leftRightKey.setFitWidth(LEFT_RIGHT_KEY_WIDTH);
                leftRightKey.setFitHeight(LEFT_RIGHT_KEY_HEIGHT);
                leftRightKey.setX(LEFT_RIGHT_KEY_X);
                leftRightKey.setY(LEFT_RIGHT_KEY_Y);

                // Jump Text
                ImageView jumpText = new ImageView(new Image(getClass().getResourceAsStream("../assets/texts/jumpText.png")));
                jumpText.setFitWidth(JUMP_TEXT_WIDTH);
                jumpText.setFitHeight(JUMP_TEXT_HEIGHT);
                jumpText.setX(JUMP_TEXT_X);
                jumpText.setY(JUMP_TEXT_Y);

                // Jump Key Gif
                ImageView jumpKeyGif = new ImageView(new Image(getClass().getResourceAsStream("../assets/objects/jumpKey.gif")));
                jumpKeyGif.setFitWidth(JUMP_KEY_GIF_WIDTH);
                jumpKeyGif.setFitHeight(JUMP_KEY_GIF_HEIGHT);
                jumpKeyGif.setX(JUMP_KEY_GIF_X);
                jumpKeyGif.setY(JUMP_KEY_GIF_Y);

                // Goal Text
                ImageView goalText = new ImageView(new Image(getClass().getResourceAsStream("../assets/texts/goalText.png")));
                goalText.setFitWidth(GOAL_TEXT_WIDTH);
                goalText.setFitHeight(GOAL_TEXT_HEIGHT);
                goalText.setX(GOAL_TEXT_X);
                goalText.setY(GOAL_TEXT_Y);

                // Goal Arrow
                ImageView goalArrowPng = new ImageView(new Image(getClass().getResourceAsStream("../assets/objects/goalArrow.png")));
                goalArrowPng.setFitWidth(GOAL_ARROW_PNG_WIDTH);
                goalArrowPng.setFitHeight(GOAL_ARROW_PNG_HEIGHT);
                goalArrowPng.setX(GOAL_ARROW_PNG_X);
                goalArrowPng.setY(GOAL_ARROW_PNG_Y);
                
                // Start Sign
                ImageView startFlagPng = new ImageView(new Image(getClass().getResourceAsStream("../assets/objects/startFlag.gif")));
                startFlagPng.setFitWidth(START_FLAG_PNG_WIDTH);
                startFlagPng.setFitHeight(START_FLAG_PNG_HEIGHT);
                startFlagPng.setX(START_FLAG_PNG_X);
                startFlagPng.setY(START_FLAG_PNG_Y);

                // add instructional assets to the root
                this.root.getChildren().addAll(tutorialHeader, moveText, leftRightKey, jumpText, jumpKeyGif, goalText, goalArrowPng, startFlagPng);



                //instantiate an animation timer for single player
                this.gametimer = new GameTimer(this.gc, this.scene, lvlSprites, isMultiplayer, null, null, null, this.spriteType, null);
            } else {
                //instantiate an animation timer for multiplayer
                this.gametimer = new GameTimer(this.gc, this.scene, lvlSprites, this.isMultiplayer, this.chatType, this.nameOfUser, this.ipAddress, this.spriteType, this.chat);
            }
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

    // Method to go back to the main menu
    private EventHandler<ActionEvent> goBackToMainMenu(){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                if(isMultiplayer){
                    // Must close the chat gui and the sockets
                    PickSpriteStage.closeChatGUIStage();
                    chat.closeChatClient();
                    if (chatType == ChatGUI.SERVER) chat.closeChatServer();
                }

                // Stop the timer
                gametimer.stop();
                // Close the music
                mediaPlayer.stop();
                // Close the current level stage
                Level.getStage().close();
                // When the close button is clicked, the createMainGUI method will be called
                Main.createMainGUI(Main.mainStage);
                // Play the Main Menu music as well
                MainGUIController.playBackgroundMusic(MainGUIController.MENU_MUSIC, SettingsStage.musicVolume);
            } 
        };
        return event;
    }
}
