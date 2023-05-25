package application;

import sprites.*;
import sprites.objects.DoorSprite;
import sprites.players.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private HashMap<String,Integer> playerTimeFinished;
    // We need to keep track of the time from the start of the game to the end of the game
    private long startTime;
    private int doorIndexX;
    private int doorIndexY;
    private Boolean isWoodSpriteFinished;
    private Boolean isSlimeSpriteFinished;
    private Boolean isCandySpriteFinished;
    private Boolean isIceSpriteFinished;

    // Multiplayer-related variables
    private ServerSocket serverSocket;
    private Boolean isMultiplayer;
    private String chatType;
    private String nameOfUser;
    private String ipAddress;
    private Integer serverPort = 5002;
    private static String spriteType;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private BufferedReader inputReader;
    private PrintWriter outputWriter;

    public static final int FPS = 60;

    /*
     * TO ADD:
     * Timers for sprites
     * Background image for game stage and sprites
     */

    GameTimer(GraphicsContext gc, Scene theScene, Sprite[][] lvlSprites, Boolean isMultiplayer, String chatType, String nameOfUser, String ipAddress, String type) {
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
        this.playerTimeFinished = new HashMap<String,Integer>();
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
            
        } if (this.isMultiplayer && this.chatType.equals(ChatGUI.CLIENT)) {
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
            InetAddress address = InetAddress.getByName(this.ipAddress);
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(new InetSocketAddress(address, serverPort));
            System.out.println("Game server started. Waiting for players...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player connected.");

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
                System.out.println("Closing server socket...");
                serverSocket.close();
            } catch (Exception e) {
                System.out.println("Unable to close server socket: " + e.getMessage());
            }
        }
    }

    // method to handle the client for multiplayer
    private static void handleClient(Socket clientSocket) {
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

    // method to broadcast the message to all connected clients (the key pressed by the player)
    private static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    // method to start the client for multiplayer. It will handle the key press events for the player and send the key pressed as well to the server
    private void startClient() {
        try {
            InetAddress address = InetAddress.getByName(this.ipAddress);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, serverPort));
            System.out.println(this.nameOfUser + " connected to server.");

            inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputWriter = new PrintWriter(socket.getOutputStream(), true);

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {

                        String message = inputReader.readLine();
                        System.out.println("Message received: " + message);
                        if (!pressed.contains(spriteType) && !pressed.contains(message) && !pressed.contains("released")){ // if the key pressed is not from our own sprite type, then we can add it to the pressed list
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

                        
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // this is to handle the key press events for the player. We need to send the key pressed to the server
            this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    KeyCode code = e.getCode();
                    outputWriter.println(spriteType  + ": " + code.getName());
                    System.out.println("Sent: "+ spriteType  + ": " + code.getName());
                    if (!pressed.contains(spriteType  + ": " + code)){
                        pressed.add(spriteType  + ": " + code);
                    }
                }
            });
    
            // this is to handle the key release events for the player. We need to send the key released to the server
            this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    KeyCode code = e.getCode();
                    pressed.remove(spriteType  + ": " + code);
                    // // we need to let the server know that the key has been released
                    outputWriter.println(spriteType  + ": " + code.getName() + " released");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

     @Override
     public void handle(long currentNanoTime) {
         this.gc.clearRect(0, 0, Level.WINDOW_WIDTH,Level.WINDOW_HEIGHT);
        
        // Update the time
        long currentSec = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
		long gameStartSec = TimeUnit.NANOSECONDS.toSeconds(this.startTime);
		int passedTime = (int) (currentSec - gameStartSec);

        // System.out.println("Passed Time: " + passedTime + " seconds");
        

        // Move the sprites
        moveMySprite(this.isMultiplayer, spriteType);
        this.woodSprite.move();
        this.slimeSprite.move();
        this.candySprite.move();
        this.iceSprite.move();

        System.out.println(pressed);
        /*
         * TO ADD:
         * Moving other sprites
         */

        // render the sprites
        for (int i = 0; i < Level.LEVEL_HEIGHT; i++) {
            for (int j = 0; j < Level.LEVEL_WIDTH; j++) {
                if(lvlSprites[i][j] instanceof WoodSprite && isWoodSpriteFinished 
                || lvlSprites[i][j] instanceof SlimeSprite && isSlimeSpriteFinished 
                || lvlSprites[i][j] instanceof CandySprite && isCandySpriteFinished 
                || lvlSprites[i][j] instanceof IceSprite && isIceSpriteFinished){
                    // do not render the sprite if it is finished and the player has reached the end of the level
                }
                else if (lvlSprites[i][j] != null)
                    lvlSprites[i][j].render(this.gc);
            }
        }

        // Check if the game is over. If it is over, we update the end time
        // If the game is over, we display the game over screen
        if(isGameOver(passedTime)) {
            this.stop(); // stop the gametimer
            Level.setGameOver(this.playerRanking, this.playerTimeFinished);
        }
        // After the sprites have been rendered, check if the player sprites have reached the end of the level, which is colliding with the Door sprite
        // Also pass the time it took for the player to reach the end of the level
        this.checkDoorCollision(passedTime);

        
        // // Put thread to sleep until next frame
        // try {
        //     Thread.sleep((1000 / GameTimer.FPS) - ((System.nanoTime() - currentNanoTime) / 1000000));
        // } catch (Exception e) {}

        

    } // end of handle method

    // method that will listen and handle the key press events
    private void handleKeyPressEvent() {
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
            	if (!pressed.contains(spriteType  + ": " + code))
            		pressed.add(spriteType  + ": " + code);
            }
        });

        this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
            	KeyCode code = e.getCode();
            	pressed.remove(spriteType  + ": " + code);
            }
        });
    }

    //method that will move the sprite depending on the key pressed
	private void moveMySprite(Boolean isMultiplayer, String spriteType) {
        if(!isMultiplayer){ 
        // Singleplayer mode

        switch (spriteType) {
            case WoodSprite.SPRITE_NAME:
                // Wood Sprite movement
                if (pressed.contains(spriteType  + ": " + KeyCode.W)) this.woodSprite.jump();
                if (pressed.contains(spriteType  + ": " + KeyCode.A) && pressed.contains(spriteType  + ": " + KeyCode.D)) this.woodSprite.setDX(0);
                else if (pressed.contains(spriteType  + ": " + KeyCode.A)) this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(spriteType  + ": " + KeyCode.D)) this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.woodSprite.setDX(0);
                break;
            case SlimeSprite.SPRITE_NAME:
                // Slime Sprite movement
                if (pressed.contains(spriteType  + ": " + KeyCode.T)) this.slimeSprite.jump();
                if (pressed.contains(spriteType  + ": " + KeyCode.F) && pressed.contains(spriteType  + ": " + KeyCode.H)) this.slimeSprite.setDX(0);
                else if (pressed.contains(spriteType  + ": " + KeyCode.F)) this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(spriteType  + ": " + KeyCode.H)) this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.slimeSprite.setDX(0);
                    
                break;
            case CandySprite.SPRITE_NAME:
                // Candy Sprite movement
                if (pressed.contains(spriteType  + ": " + KeyCode.I)) this.candySprite.jump();
                if (pressed.contains(spriteType  + ": " + KeyCode.J) && pressed.contains(spriteType  + ": " + KeyCode.L)) this.candySprite.setDX(0);
                else if (pressed.contains(spriteType  + ": " + KeyCode.J)) this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(spriteType  + ": " + KeyCode.L)) this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.candySprite.setDX(0);
                        
                break;
            case IceSprite.SPRITE_NAME:
                // Ice Sprite movement
                if (pressed.contains(spriteType  + ": " + KeyCode.UP)) this.iceSprite.jump();
                if (pressed.contains(spriteType  + ": " + KeyCode.LEFT) && pressed.contains(spriteType  + ": " + KeyCode.RIGHT)) this.iceSprite.setDX(0);
                else if (pressed.contains(spriteType  + ": " + KeyCode.LEFT)) this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(spriteType  + ": " + KeyCode.RIGHT)) this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.iceSprite.setDX(0);
                            
                break;

            default:
                break;
        }

        } else {
            // For multiplayer, there's only one set of player sprite movement control keys
            // Get the sprite type of the player

                // Wood Sprite movement
                if (pressed.contains(WoodSprite.SPRITE_NAME  + ": " + KeyCode.W)) this.woodSprite.jump();
                if (pressed.contains(WoodSprite.SPRITE_NAME  + ": " + KeyCode.A) && pressed.contains(WoodSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.woodSprite.setDX(0);
                else if (pressed.contains(WoodSprite.SPRITE_NAME  + ": " + KeyCode.A)) this.woodSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(WoodSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.woodSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.woodSprite.setDX(0);

                // Slime Sprite movement
                if (pressed.contains(SlimeSprite.SPRITE_NAME  + ": " + KeyCode.W)) this.slimeSprite.jump();
                if (pressed.contains(SlimeSprite.SPRITE_NAME  + ": " + KeyCode.A) && pressed.contains(SlimeSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.slimeSprite.setDX(0);
                else if (pressed.contains(SlimeSprite.SPRITE_NAME  + ": " + KeyCode.A)) this.slimeSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(SlimeSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.slimeSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.slimeSprite.setDX(0);

                // Candy Sprite movement
                if (pressed.contains(CandySprite.SPRITE_NAME  + ": " + KeyCode.W)) this.candySprite.jump();
                if (pressed.contains(CandySprite.SPRITE_NAME  + ": " + KeyCode.A) && pressed.contains(CandySprite.SPRITE_NAME  + ": " + KeyCode.D)) this.candySprite.setDX(0);
                else if (pressed.contains(CandySprite.SPRITE_NAME  + ": " + KeyCode.A)) this.candySprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(CandySprite.SPRITE_NAME  + ": " + KeyCode.D)) this.candySprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.candySprite.setDX(0);

                // Ice Sprite movement
                if (pressed.contains(IceSprite.SPRITE_NAME  + ": " + KeyCode.W)) this.iceSprite.jump();
                if (pressed.contains(IceSprite.SPRITE_NAME  + ": " + KeyCode.A) && pressed.contains(IceSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.iceSprite.setDX(0);
                else if (pressed.contains(IceSprite.SPRITE_NAME  + ": " + KeyCode.A)) this.iceSprite.setDX(-PlayerSprite.MOVE_DISTANCE);
                else if (pressed.contains(IceSprite.SPRITE_NAME  + ": " + KeyCode.D)) this.iceSprite.setDX(PlayerSprite.MOVE_DISTANCE);
                else this.iceSprite.setDX(0);
        

        }
   	}

    // method to print out the details of the sprite
    // private void printSpriteDetails() {
        // System.out.println("=============== SPRITE DETAILS ==============");
        // int xCoord = this.woodSprite.getCenterX();
        // int yCoord = this.woodSprite.getCenterY();
        // int xIndex = xCoord / (Level.WINDOW_WIDTH / Level.LEVEL_WIDTH);
        // int yIndex = yCoord / (Level.WINDOW_HEIGHT / Level.LEVEL_HEIGHT);
        // System.out.println("========================");
        // System.out.println("Sprite X-coordinate:" + xCoord);
        // System.out.println("Sprite Y-coordinate:" + yCoord);
        // System.out.println("Sprite X-index:" + xIndex);
        // System.out.println("Sprite Y-index:" + yIndex);
        // System.out.println("Sprite Collides with:" + this.lvlSprites[yIndex][xIndex]);
        // System.out.println("Collission? " + this.woodSprite.collidesWith(this.lvlSprites[yIndex][xIndex]));

    // }

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

    // method to check if the sprite is colliding with the door. If it is, add the sprite to the player ranking and update the time for the sprite to finish the level
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

        if (woodSpriteYIndex == this.doorIndexY && woodSpriteXIndex == this.doorIndexX && !this.playerRanking.contains(WoodSprite.SPRITE_NAME)) {
            this.playerRanking.add(WoodSprite.SPRITE_NAME);
            this.playerTimeFinished.put(WoodSprite.SPRITE_NAME, timeFinished);
            this.isWoodSpriteFinished = true;
        }
        if (slimeSpriteYIndex == this.doorIndexY && slimeSpriteXIndex == this.doorIndexX && !this.playerRanking.contains(SlimeSprite.SPRITE_NAME)) {
            this.playerRanking.add(SlimeSprite.SPRITE_NAME);
            this.playerTimeFinished.put(SlimeSprite.SPRITE_NAME, timeFinished);
            this.isSlimeSpriteFinished = true;
        }
        if (candySpriteYIndex == this.doorIndexY && candySpriteXIndex == this.doorIndexX && !this.playerRanking.contains(CandySprite.SPRITE_NAME)) {
            this.playerRanking.add(CandySprite.SPRITE_NAME);
            this.playerTimeFinished.put(CandySprite.SPRITE_NAME, timeFinished);
            this.isCandySpriteFinished = true;
        }
        if (iceSpriteYIndex == this.doorIndexY && iceSpriteXIndex == this.doorIndexX && !this.playerRanking.contains(IceSprite.SPRITE_NAME)) {
            this.playerRanking.add(IceSprite.SPRITE_NAME);
            this.playerTimeFinished.put(IceSprite.SPRITE_NAME, timeFinished);
            this.isIceSpriteFinished = true;
        }

    }

    // method to check if the game is over, which means all the players have finished the level
    private boolean isGameOver(int timeFinished) {
        return this.isWoodSpriteFinished && this.isSlimeSpriteFinished && this.isCandySpriteFinished && this.isIceSpriteFinished;
    }
}
