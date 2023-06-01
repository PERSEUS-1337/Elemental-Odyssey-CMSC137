package application;

import sprites.*;
import sprites.objects.DoorSprite;
import sprites.players.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameTimer extends AnimationTimer {
    private GraphicsContext gc;
    private Scene theScene;
    private WoodSprite woodSprite;
    private SlimeSprite slimeSprite;
    private CandySprite candySprite;
    private IceSprite iceSprite;
    private Sprite[][] lvlSprites;
    private ArrayList<String> pressed;

    // GameOver-related variables
    private ArrayList<String> playerRanking;
    private HashMap<String, String> playerTimeFinished; // We need to keep track of the ranking of player sprites
    private Integer rankCounter;
    private long startTime;
    private int doorIndexX;
    private int doorIndexY;
    private Boolean isWoodSpriteFinished;
    private Boolean isSlimeSpriteFinished;
    private Boolean isCandySpriteFinished;
    private Boolean isIceSpriteFinished;

    // Multiplayer-related variables
    private ServerSocket serverSocket;
    private Socket socket;
    private Boolean isMultiplayer;
    private String chatType;
    private String nameOfUser;
    private String ipAddress;
    private Integer serverPort = 53412;     // Port number for the game server 
    private static String spriteType;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    public static final int FPS = 60;

    GameTimer(GraphicsContext gc, Scene theScene, Sprite[][] lvlSprites, Boolean isMultiplayer, String chatType,
            String nameOfUser, String ipAddress, String type) {
        this.gc = gc;
        this.theScene = theScene;
        this.lvlSprites = lvlSprites;
        this.pressed = new ArrayList<String>();

        // Multiplayer-related variables
        this.isMultiplayer = isMultiplayer;
        this.chatType = chatType;
        this.nameOfUser = nameOfUser;
        this.ipAddress = ipAddress;
        spriteType = type;

        // Initialize GameOver-related variables
        this.playerRanking = new ArrayList<String>();
        this.playerTimeFinished = new HashMap<String, String>();
        this.rankCounter = 0;
        this.startTime = System.nanoTime();

        // Get the index of the door based on the lvldata to get the door sprite
        this.locateDoor();

        // Initialize the isFinished variables
        this.isWoodSpriteFinished = false;
        this.isSlimeSpriteFinished = false;
        this.isCandySpriteFinished = false;
        this.isIceSpriteFinished = false;

        // Get variable reference to player sprites
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if (lvlSprites[i][j] instanceof WoodSprite)
                    this.woodSprite = (WoodSprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof SlimeSprite)
                    this.slimeSprite = (SlimeSprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof CandySprite)
                    this.candySprite = (CandySprite) lvlSprites[i][j];
                else if (lvlSprites[i][j] instanceof IceSprite)
                    this.iceSprite = (IceSprite) lvlSprites[i][j];
            }
        }

        // give reference of lvlSprites to players to interact with surroundings
        this.woodSprite.setLevelData(lvlSprites);
        this.slimeSprite.setLevelData(lvlSprites);
        this.candySprite.setLevelData(lvlSprites);
        this.iceSprite.setLevelData(lvlSprites);

        // If the game is multiplayer, we need to create a new thread for the server
        if (this.isMultiplayer && this.chatType.equals(ChatGUI.SERVER)) {
            // Create a new thread for the server
            Thread serverThread = new Thread(this::startServer);
            serverThread.start();
            Thread clientThread = new Thread(this::startClient);
            clientThread.start();

        }
        if (this.isMultiplayer && this.chatType.equals(ChatGUI.CLIENT)) {
            // Create a new thread for the client
            Thread clientThread = new Thread(this::startClient);
            clientThread.start();
        } else { // if the game is singleplayer, we need to handle the key press events
            // this.handleKeyPressEvent();
        }

    } // end of constructor

    // method to start the server for multiplayer
    private void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
            System.out.println("Game server started. Waiting for players...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Game Server: Player connected.");

                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(clientWriter);

                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally { // close the server socket
            try {
                System.out.println("Game Server: Closing server socket...");
                serverSocket.close();
            } catch (Exception e) {
                System.out.println("Game Server: Unable to close server socket - " + e.getMessage());
            }
        }
    }

    // method to handle the client for multiplayer
    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                String message = inputReader.readLine();
                if (message == null) {
                    // Client disconnected
                    break;
                }

                // Broadcast the key press to all connected clients
                broadcast(message);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    // method to broadcast the message to all connected clients (the key pressed by
    // the player)
    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // method to start the client for multiplayer. It will handle the key press
    // events for the player and send the key pressed as well to the server
    private void startClient() {
        try {
            socket = new Socket(ipAddress, this.serverPort);
            System.out.println("Game Server: " + this.nameOfUser + " connected to server.");

            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> { // This thread is responsible for receiving the sprite movements
                try {
                    while (true) {

                        String message = inputReader.readLine(); // read the message sent by the server

                        if (!pressed.contains(spriteType) && !pressed.contains(message)
                                && !pressed.contains("released")) {
                                // if the key pressed is not from our own sprite
                                // type, then we can add it to the pressed list
                            pressed.add(message);
                        } 
                        
                        if (message.contains("released")) {
                            // if the key pressed is released, then we need to remove it from the pressed list
                            // the key pressed is in the format of "spriteType: keyName released"
                            String[] messageSplit = message.split(" ");
                            String keyName = messageSplit[1];
                            String spriteType = messageSplit[0];
                            pressed.removeIf(key -> key.contains(keyName) && key.contains(spriteType));
                        }

                        // Extract x coordinate of the sprite
                        String xCoordinate = "";
                        Pattern xPattern = Pattern.compile("x: (\\d+)");
                        Matcher xMatcher = xPattern.matcher(message);
                        if (xMatcher.find()) {
                            xCoordinate = xMatcher.group(1);
                        }

                        // Extract y coordinate of the sprite
                        String yCoordinate = "";
                        Pattern yPattern = Pattern.compile("y: (\\d+)");
                        Matcher yMatcher = yPattern.matcher(message);
                        if (yMatcher.find()) {
                            yCoordinate = yMatcher.group(1);
                        }


                        // Setting the coordinates of the other sprites not controlled by the current player
                        if (message.contains("candySprite") && spriteType != "CandySprite") {
                            this.candySprite.setX(Integer.parseInt(xCoordinate));
                            this.candySprite.setY(Integer.parseInt(yCoordinate));
                        } else if (message.contains("iceSprite") && spriteType != "IceSprite") {
                            this.iceSprite.setX(Integer.parseInt(xCoordinate));
                            this.iceSprite.setY(Integer.parseInt(yCoordinate));
                        } else if (message.contains("slimeSprite") && spriteType != "SlimeSprite") {
                            this.slimeSprite.setX(Integer.parseInt(xCoordinate));
                            this.slimeSprite.setY(Integer.parseInt(yCoordinate));
                        } else if (message.contains("woodSprite") && spriteType != "WoodSprite") {
                            this.woodSprite.setX(Integer.parseInt(xCoordinate));
                            this.woodSprite.setY(Integer.parseInt(yCoordinate));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // this is to handle the key press events for the player. We need to send the
            // key pressed to the server
            this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    KeyCode code = e.getCode();
                    if (!pressed.contains(spriteType + ": " + code)) {
                        pressed.add(spriteType + ": " + code);
                    }
                }
            });

            // this is to handle the key release events for the player. We need to send the
            // key released to the server
            this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    KeyCode code = e.getCode();
                    pressed.remove(spriteType + ": " + code);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void handle(long currentNanoTime) {
        this.gc.clearRect(0, 0, Level.WINDOW_WIDTH, Level.WINDOW_HEIGHT);

        // Update the time
        long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
        long gameStartSec = TimeUnit.NANOSECONDS.toSeconds(this.startTime);
        int passedTime = (int) (currentSec - gameStartSec);

        // Move the sprites
        moveMySprite(this.isMultiplayer, spriteType);

        // Move only the sprite of the current player based on the updated coordinates
        if (spriteType == "WoodSprite") {
            this.woodSprite.move();
            outputWriter.println("woodSprite Coord = x: " + this.woodSprite.getX() + " y: " + this.woodSprite.getY());
        } else if (spriteType == "SlimeSprite") {
            this.slimeSprite.move();
            outputWriter
                    .println("slimeSprite Coord = x: " + this.slimeSprite.getX() + " y: " + this.slimeSprite.getY());
        } else if (spriteType == "CandySprite") {
            this.candySprite.move();
            outputWriter
                    .println("candySprite Coord = x: " + this.candySprite.getX() + " y: " + this.candySprite.getY());
        } else {
            this.iceSprite.move();
            outputWriter.println("iceSprite Coord = x: " + this.iceSprite.getX() + " y: " + this.iceSprite.getY());
        }

        // render the sprites
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if (lvlSprites[i][j] instanceof WoodSprite && isWoodSpriteFinished
                        || lvlSprites[i][j] instanceof SlimeSprite && isSlimeSpriteFinished
                        || lvlSprites[i][j] instanceof CandySprite && isCandySpriteFinished
                        || lvlSprites[i][j] instanceof IceSprite && isIceSpriteFinished) {
                    // do not render the sprite if it is finished and the player has reached the end
                    // of the level
                } else if (lvlSprites[i][j] != null)
                    lvlSprites[i][j].render(this.gc);
            }
        }

        // Check if the game is over (must get the first tree sprites). If it is over, we update the end time
        // If the game is over, we display the game over screen
        if (this.rankCounter == 3) {
            this.stop(); // stop the gametimer
            Level.setGameOver(this.playerRanking, this.playerTimeFinished);
        }
        // After the sprites have been rendered, check if the player sprites have
        // reached the end of the level, which is colliding with the Door sprite
        // Also pass the time it took for the player to reach the end of the level
        this.checkDoorCollision(passedTime);

    } // end of handle method

    // method that will move the sprite depending on the key pressed
    private void moveMySprite(Boolean isMultiplayer, String spriteType) {

        switch (spriteType) {
            case WoodSprite.SPRITE_NAME:
                // Wood Sprite movement
                if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.woodSprite.jump();
                if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.woodSprite.setDX(0);
                else if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.A))
                    this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else
                    this.woodSprite.setDX(0);
                break;
            case SlimeSprite.SPRITE_NAME:
                // Slime Sprite movement
                if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.slimeSprite.jump();
                if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.slimeSprite.setDX(0);
                else if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.A))
                    this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else
                    this.slimeSprite.setDX(0);

                break;
            case CandySprite.SPRITE_NAME:
                // Candy Sprite movement
                if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.candySprite.jump();
                if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.candySprite.setDX(0);
                else if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.A))
                    this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else
                    this.candySprite.setDX(0);

                break;
            case IceSprite.SPRITE_NAME:
                // Ice Sprite movement
                if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.iceSprite.jump();
                if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.iceSprite.setDX(0);
                else if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.A))
                    this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.D))
                    this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else
                    this.iceSprite.setDX(0);

                break;

            default:
                break;
        }
    }

    // method to locate the door sprite in the level data. It updates the doorIndexY and doorIndexX
    private void locateDoor() {
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if (this.lvlSprites[i][j] instanceof DoorSprite) {
                    this.doorIndexY = i;
                    this.doorIndexX = j;
                }
            }
        }
    }

    // method to check if the sprite is colliding with the door. If it is, add the
    // sprite to the player ranking and update the time for the sprite to finish the
    // level
    private void checkDoorCollision(int timeFinished) {
        // Calculate first the yIndex and xIndex of each player sprite
        int woodSpriteXCoord = this.woodSprite.getCenterX();
        int woodSpriteYCoord = this.woodSprite.getCenterY();
        int woodSpriteXIndex = woodSpriteXCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        int woodSpriteYIndex = woodSpriteYCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);

        int slimeSpriteXCoord = this.slimeSprite.getCenterX();
        int slimeSpriteYCoord = this.slimeSprite.getCenterY();
        int slimeSpriteXIndex = slimeSpriteXCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        int slimeSpriteYIndex = slimeSpriteYCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);

        int candySpriteXCoord = this.candySprite.getCenterX();
        int candySpriteYCoord = this.candySprite.getCenterY();
        int candySpriteXIndex = candySpriteXCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        int candySpriteYIndex = candySpriteYCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);

        int iceSpriteXCoord = this.iceSprite.getCenterX();
        int iceSpriteYCoord = this.iceSprite.getCenterY();
        int iceSpriteXIndex = iceSpriteXCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        int iceSpriteYIndex = iceSpriteYCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);

        if (woodSpriteYIndex == this.doorIndexY && woodSpriteXIndex == this.doorIndexX
                && !this.playerRanking.contains(WoodSprite.SPRITE_NAME)) {
            this.playerRanking.add(WoodSprite.SPRITE_NAME);
            this.playerTimeFinished.put(WoodSprite.SPRITE_NAME, Integer.toString(++this.rankCounter));
            this.isWoodSpriteFinished = true;
        }
        if (slimeSpriteYIndex == this.doorIndexY && slimeSpriteXIndex == this.doorIndexX
                && !this.playerRanking.contains(SlimeSprite.SPRITE_NAME)) {
            this.playerRanking.add(SlimeSprite.SPRITE_NAME);
            this.playerTimeFinished.put(SlimeSprite.SPRITE_NAME, Integer.toString(++this.rankCounter));
            this.isSlimeSpriteFinished = true;
        }
        if (candySpriteYIndex == this.doorIndexY && candySpriteXIndex == this.doorIndexX
                && !this.playerRanking.contains(CandySprite.SPRITE_NAME)) {
            this.playerRanking.add(CandySprite.SPRITE_NAME);
            this.playerTimeFinished.put(CandySprite.SPRITE_NAME, Integer.toString(++this.rankCounter));
            this.isCandySpriteFinished = true;
        }
        if (iceSpriteYIndex == this.doorIndexY && iceSpriteXIndex == this.doorIndexX
                && !this.playerRanking.contains(IceSprite.SPRITE_NAME)) {
            this.playerRanking.add(IceSprite.SPRITE_NAME);
            this.playerTimeFinished.put(IceSprite.SPRITE_NAME, Integer.toString(++this.rankCounter));
            this.isIceSpriteFinished = true;
        }

    }
}
