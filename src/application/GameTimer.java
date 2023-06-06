package application;

import sprites.*;
import sprites.objects.DoorSprite;
import sprites.players.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
    private ChatGUI chat;
    private Integer serverPort = 53412;     // Port number for the game server 
    private Integer playerCounter;
    private static String spriteType;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private Thread serverThread;
    private Thread clientThread;

    public static final int FPS = 60;

    GameTimer(GraphicsContext gc, Scene theScene, Sprite[][] lvlSprites, Boolean isMultiplayer, String chatType,
            String nameOfUser, String ipAddress, String type, ChatGUI chat) {
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
        this.chat = chat;
        this.playerCounter = 0;

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

        if(this.isMultiplayer){

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
        } else{

                // Get variable reference to player sprites
                for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
                    for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                        if (lvlSprites[i][j] instanceof WoodSprite)
                            this.woodSprite = (WoodSprite) lvlSprites[i][j];
                    }
                }

            // give reference of lvlSprites to the single player [WOOD SPRITE ONLY]
            this.woodSprite.setLevelData(lvlSprites);
        }

        // If the game is multiplayer, we need to create a new thread for the server
        if (this.isMultiplayer && this.chatType.equals(ChatGUI.SERVER)) {
            // Create a new thread for the server
            serverThread = new Thread(this::startServer);
            serverThread.start();
            clientThread = new Thread(this::startClient);
            clientThread.start();

        }
        else if (this.isMultiplayer && this.chatType.equals(ChatGUI.CLIENT)) {
            // Create a new thread for the client
            clientThread = new Thread(this::startClient);
            clientThread.start();
        } else { // if the game is singleplayer, we need to handle the key press events
            this.handleKeyPressEvent();
        }

    } // end of constructor

    // method to handle the key press events for the player
    private void handleKeyPressEvent() {
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                if (!pressed.contains(spriteType + ": " + code)) {
                    pressed.add(spriteType + ": " + code);
                }
            }
        });

        this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                pressed.remove(spriteType + ": " + code);
            }
        });
    }

    // method to start the server for multiplayer
    private void startServer() {
        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.setReuseAddress(true);
            this.serverSocket.bind(new InetSocketAddress(this.serverPort));
            System.out.println("Game server started. Waiting for players...");

            ArrayList<Thread> handleClientThreads = new ArrayList<Thread>();

            while (this.rankCounter < 3) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Game Server: Player connected.");
                this.playerCounter++;

                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(clientWriter);

                Thread handleClientThread = new Thread(() -> handleClient(clientSocket));
                handleClientThreads.add(handleClientThread);
                handleClientThread.start();
            }

            // for (Thread t : handleClientThreads){
            //     try {
            //         t.join();
            //     } catch (InterruptedException e) {
            //         e.printStackTrace();
            //     }
            // }
            
        } catch (Exception e) {
            if (!(e instanceof SocketException))
                e.printStackTrace();
            // System.exit(0);
        } 
        // finally { // close the server socket
        //     try {
        //         System.out.println("Game Server: Closing server socket...");
        //         serverSocket.close();
        //     } catch (Exception e) {
        //         System.out.println("Game Server: Unable to close server socket - " + e.getMessage());
        //     }
        // }
    }

    // method to handle the client for multiplayer
    private void handleClient(Socket clientSocket) {
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (this.rankCounter < 3) {
                String message = inputReader.readLine();
                if (message == null) {
                    // Client disconnected
                    break;
                }

                // Broadcast the key press to all connected clients
                broadcast(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // System.exit(0);
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
                    while (this.rankCounter < 3) {

                        String message = inputReader.readLine(); // read the message sent by the server

                        // First, let's check the message if it contains the name of the player and its sprite type
                        // which is in the format of "<TYPE: spriteType><NAME: nameOfUser>"
                       
                        // Insert the name of the player and its sprite type to the "pressed" list only if the sprite type is not the same as the other players
                        if (message.contains("<TYPE: ")) {
                            // Extract the name of the player
                            String nameOfPlayer = "";
                            Pattern namePattern = Pattern.compile("NAME: (\\w+)");
                            Matcher nameMatcher = namePattern.matcher(message);
                            if (nameMatcher.find()) {
                                nameOfPlayer = nameMatcher.group(1);
                            }

                            // Extract the sprite type of the player
                            String spriteTypeOfPlayer = "";
                            Pattern spriteTypePattern = Pattern.compile("TYPE: (\\w+)");
                            Matcher spriteTypeMatcher = spriteTypePattern.matcher(message);
                            if (spriteTypeMatcher.find()) {
                                spriteTypeOfPlayer = spriteTypeMatcher.group(1);
                            }

                            // let's find all instances of <TYPE: ... in the pressed list
                            String[] pressedArray = pressed.toArray(new String[pressed.size()]);
                            for (String key : pressedArray) {
                                if (key.contains("<TYPE: ")) {
                                    // extract the sprite type in the current key
                                    String spriteTypeInPressed = "";
                                    Pattern spriteTypePatternInPressed = Pattern.compile("TYPE: (\\w+)");
                                    Matcher spriteTypeMatcherInPressed = spriteTypePatternInPressed.matcher(key);
                                    if (spriteTypeMatcherInPressed.find()) {
                                        spriteTypeInPressed = spriteTypeMatcherInPressed.group(1);
                                    }


                                    // extract the name of the player in the current key
                                    String nameOfPlayerInPressed = "";
                                    Pattern namePatternInPressed = Pattern.compile("NAME: (\\w+)");
                                    Matcher nameMatcherInPressed = namePatternInPressed.matcher(key);
                                    if (nameMatcherInPressed.find()) {
                                        nameOfPlayerInPressed = nameMatcherInPressed.group(1);
                                    }

                                    // if the sprite type of the current key is the same as the sprite type of the player but has a different name, then it is invalid
                                    if (spriteTypeInPressed.equals(spriteTypeOfPlayer) && !nameOfPlayerInPressed.equals(nameOfPlayer)) {
                                        // Game invalid closing the game
                                        
                                        if(nameOfPlayer.equals(this.nameOfUser)){
                                            System.out.println("Game Client: Invalid game. Closing the game...");
                                            System.exit(0);
                                        }
                                    } else if (spriteTypeInPressed.equals(spriteTypeOfPlayer) && nameOfPlayerInPressed.equals(nameOfPlayer)) {
                                        // don't re-add
                                    } else {
                                        pressed.add(message);
                                    }

                                }
                            }
                            
                        }

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
                        // Remove the "coordinate" messages from the pressed list
                        pressed.removeIf(key -> key.contains("Coord"));
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

            receiveThread.join();
        } catch (Exception e) {
            e.printStackTrace();
            // System.exit(0);
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

        if(this.isMultiplayer && this.outputWriter != null){
            // Move only the sprite of the current player based on the updated coordinates
            if (spriteType == "WoodSprite") {
                this.woodSprite.move();
                outputWriter.println("woodSprite Coord = x: " + this.woodSprite.getX() + " y: " + this.woodSprite.getY());
                // Send the name of the player and its sprite type to the server
                outputWriter.println("<TYPE: " + spriteType+"><NAME: " + this.nameOfUser+">");
            } else if (spriteType == "SlimeSprite") {
                this.slimeSprite.move();
                outputWriter
                        .println("slimeSprite Coord = x: " + this.slimeSprite.getX() + " y: " + this.slimeSprite.getY());
                        outputWriter.println("<TYPE: " + spriteType+"><NAME: " + this.nameOfUser+">");
            } else if (spriteType == "CandySprite") {
                this.candySprite.move();
                outputWriter
                        .println("candySprite Coord = x: " + this.candySprite.getX() + " y: " + this.candySprite.getY());
                        outputWriter.println("<TYPE: " + spriteType+"><NAME: " + this.nameOfUser+">");
            } else {
                this.iceSprite.move();
                outputWriter.println("iceSprite Coord = x: " + this.iceSprite.getX() + " y: " + this.iceSprite.getY());
                outputWriter.println("<TYPE: " + spriteType+"><NAME: " + this.nameOfUser+">");
            }
        } else if (!this.isMultiplayer) {
            // Move the sprite of the current player
            if (spriteType == "WoodSprite") {
                this.woodSprite.move();
            } else if (spriteType == "SlimeSprite") {
                this.slimeSprite.move();
            } else if (spriteType == "CandySprite") {
                this.candySprite.move();
            } else {
                this.iceSprite.move();
            }
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
        if (this.rankCounter == 3 && this.isMultiplayer) {
            this.stop(); // stop the gametimer

            // Must close the chat gui and the sockets
            PickSpriteStage.closeChatGUIStage();
            // this.chat.closeSocket();

            try {
                // Join client thread before closing sockets
                clientThread.join();

                this.socket.close();
                System.out.println("Game Client: Closing client socket...");
                this.chat.closeChatClient();

                if (this.chatType == ChatGUI.SERVER){
                    this.serverSocket.close();  // interrupts server thread
                    System.out.println("Game Server: Closing server socket...");
                    serverThread.join();
                    this.chat.closeChatServer();
                }

                outputWriter.close();
                inputReader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Level.setGameOver(this.playerRanking, this.playerTimeFinished, this.isMultiplayer);
        } 
        
        // Check if the game is over (must get the singleplayer sprite). If it is over, we update the end time
        // If the game is over, we display the game over screen
        else if (this.rankCounter == 1 && !this.isMultiplayer) {
                this.stop(); // stop the gametimer
                Level.setGameOver(this.playerRanking, this.playerTimeFinished, this.isMultiplayer);
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
                        && pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.D)){
                            this.woodSprite.setDX(0);
                            this.woodSprite.setFlipped(false); // set the sprite to face right
                        }
                    
                else if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.A)){
                    this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                    this.woodSprite.setFlipped(true); // set the sprite to face left
                }
                else if (pressed.contains(WoodSprite.SPRITE_NAME + ": " + KeyCode.D)){
                    this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                    this.woodSprite.setFlipped(false); // set the sprite to face right
                }
                else
                    this.woodSprite.setDX(0);
                break;
            case SlimeSprite.SPRITE_NAME:
                // Slime Sprite movement
                if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.slimeSprite.jump();
                if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.D)){
                            this.slimeSprite.setDX(0);
                            this.slimeSprite.setFlipped(false); // set the sprite to face right
                        }
                    
                else if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.A)){
                    this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                    this.slimeSprite.setFlipped(true); // set the sprite to face left
                }
                else if (pressed.contains(SlimeSprite.SPRITE_NAME + ": " + KeyCode.D)){
                    this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                    this.slimeSprite.setFlipped(false); // set the sprite to face right
                }
                else
                    this.slimeSprite.setDX(0);

                break;
            case CandySprite.SPRITE_NAME:
                // Candy Sprite movement
                if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.candySprite.jump();
                if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.D)){
                            this.candySprite.setDX(0);
                            this.candySprite.setFlipped(false); // set the sprite to face right
                        }
                else if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.A)){
                    this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                    this.candySprite.setFlipped(true); // set the sprite to face left
                }
                else if (pressed.contains(CandySprite.SPRITE_NAME + ": " + KeyCode.D)){
                    this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
                    this.candySprite.setFlipped(false); // set the sprite to face right
                }
                else
                    this.candySprite.setDX(0);

                break;
            case IceSprite.SPRITE_NAME:
                // Ice Sprite movement
                if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.W))
                    this.iceSprite.jump();
                if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.A)
                        && pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.D)){
                            this.iceSprite.setDX(0);
                            this.iceSprite.setFlipped(false); // set the sprite to face right
                        }
                else if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.A)){
                    this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                    this.iceSprite.setFlipped(true); // set the sprite to face left
                }
                else if (pressed.contains(IceSprite.SPRITE_NAME + ": " + KeyCode.D)){
                    this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                    this.iceSprite.setFlipped(false); // set the sprite to face right
                }
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
        if(this.isMultiplayer){
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
        } else {
            // Calculate first the yIndex and xIndex of the single player sprite
            int woodSpriteXCoord = this.woodSprite.getCenterX();
            int woodSpriteYCoord = this.woodSprite.getCenterY();
            int woodSpriteXIndex = woodSpriteXCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
            int woodSpriteYIndex = woodSpriteYCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);

            if (woodSpriteYIndex == this.doorIndexY && woodSpriteXIndex == this.doorIndexX
                    && !this.playerRanking.contains(WoodSprite.SPRITE_NAME)) {
                this.playerRanking.add(WoodSprite.SPRITE_NAME);
                this.playerTimeFinished.put(WoodSprite.SPRITE_NAME, Integer.toString(++this.rankCounter));
                this.isWoodSpriteFinished = true;
            }
        }

    }
}
