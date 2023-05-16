package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatGUI {
    private TextArea msgArea;
    private TextField msgInput;
    private Button msgSend;
    private Socket socket;
    private ServerSocket server;
    private OutputStreamWriter writer;
    private String chatterName;
    private String serverIP;
    private Integer serverPort;
    private Integer numClients;

    public static final String SERVER = "server";
    public static final String CLIENT = "client";

    public ChatGUI (String chatType, String chatName){
        this.chatterName = chatName;
        this.numClients = 1;
        this.serverIP = "localhost";
        this.serverPort = 5000;

        if(chatType.equals(SERVER)){
            serverListen();
        } else if(chatType.equals(CLIENT)){
            clientListen();
        }
    }

    // Method for the client to listen to the server
    private void clientListen() {
        try {
            socket = new Socket(this.serverIP, serverPort);
            System.out.println("Connected to server at port " + serverPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            Thread receivingThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = reader.readLine();
                        if (message == null) {
                            break;
                        }
                        appendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            receivingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // Method for the server to listen to the client
    private void serverListen() {
        try {
            this.server = new ServerSocket(this.serverPort);
            System.out.println("Server instantiated at port " + this.serverPort);
            System.out.println("Waiting for client(s) to connect...");

            int counterIndex = 0;
            // while the server is running, accept connections from clients
            while(counterIndex != this.numClients){
                this.socket = server.accept();
                System.out.println("Client connected using port " + this.socket.getPort());
                counterIndex++;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            Thread receivingThread = new Thread(() -> {
                try {
                    while (true) {
                        String message = reader.readLine();
                        if (message == null) {
                            break;
                        }
                        appendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            receivingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // Method to send a message to the server
    private void sendMessage(String message) {
        try {
            writer.write(this.chatterName + ": " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to append a message to the message area
    private void appendMessage(String message) {
        msgArea.appendText(message + "\n");
    }

    void setStage(Stage primaryStage) {
        primaryStage.setTitle("Game Chat");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(328.0, 527.0);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(328.0, 530.0);

        ImageView chatAppBackground = new ImageView(new Image(getClass().getResourceAsStream("../assets/background/chatBackground.png")));
        chatAppBackground.setFitHeight(530.0);
        chatAppBackground.setFitWidth(328.0);
        chatAppBackground.setPickOnBounds(true);

        ImageView lowerLeftCorner = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/lowerLeftCorner_Gold.png")));
        lowerLeftCorner.setFitHeight(23.0);
        lowerLeftCorner.setFitWidth(34.0);
        lowerLeftCorner.setLayoutY(504.0);
        lowerLeftCorner.setPickOnBounds(true);
        lowerLeftCorner.setPreserveRatio(true);

        ImageView upperRightCorner = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/upperRightCorner_Gold.png")));
        upperRightCorner.setFitHeight(23.0);
        upperRightCorner.setFitWidth(31.0);
        upperRightCorner.setLayoutX(297.0);
        upperRightCorner.setPickOnBounds(true);
        upperRightCorner.setPreserveRatio(true);

        ImageView upperLeftCorner = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/upperLeftCorner_Gold.png")));
        upperLeftCorner.setFitHeight(35.0);
        upperLeftCorner.setFitWidth(31.0);
        upperLeftCorner.setPickOnBounds(true);
        upperLeftCorner.setPreserveRatio(true);

        ImageView lowerRightCorner = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/lowerRightCorner_Gold.png")));
        lowerRightCorner.setFitHeight(23.0);
        lowerRightCorner.setFitWidth(31.0);
        lowerRightCorner.setLayoutX(297.0);
        lowerRightCorner.setLayoutY(504.0);
        lowerRightCorner.setPickOnBounds(true);
        lowerRightCorner.setPreserveRatio(true);

        ImageView leftBorder = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/longVerticalGoldBorder.png")));
        leftBorder.setFitHeight(481.0);
        leftBorder.setFitWidth(51.0);
        leftBorder.setLayoutY(23.0);
        leftBorder.setPickOnBounds(true);
        leftBorder.setPreserveRatio(true);

        ImageView rightBorder = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/longVerticalGoldBorder.png")));
        rightBorder.setFitHeight(481.0);
        rightBorder.setFitWidth(51.0);
        rightBorder.setLayoutX(323.0);
        rightBorder.setLayoutY(23.0);
        rightBorder.setPickOnBounds(true);
        rightBorder.setPreserveRatio(true);

        ImageView topBorder = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/longHorizontalGoldBorder.png")));
        topBorder.setFitHeight(7.0);
        topBorder.setFitWidth(269.0);
        topBorder.setLayoutX(30.0);
        topBorder.setPickOnBounds(true);
        topBorder.setPreserveRatio(true);

        ImageView bottomBorder = new ImageView(new Image(getClass().getResourceAsStream("../assets/blocks/longHorizontalGoldBorder.png")));
        bottomBorder.setFitHeight(7.0);
        bottomBorder.setFitWidth(269.0);
        bottomBorder.setLayoutX(30.0);
        bottomBorder.setLayoutY(524.0);
        bottomBorder.setPickOnBounds(true);
        bottomBorder.setPreserveRatio(true);
    
        Pane pane = new Pane();
        pane.setLayoutX(1.0);
        pane.setLayoutY(22.0);
        pane.setPrefHeight(481.0);
        pane.setPrefWidth(322.0);
    
        msgArea = new TextArea();
        msgArea.setLayoutX(18.0);
        msgArea.setLayoutY(14.0);
        msgArea.setPrefHeight(397.0);
        msgArea.setPrefWidth(290.0);
    
        msgInput = new TextField();
        msgInput.setLayoutX(18.0);
        msgInput.setLayoutY(435.0);
        msgInput.setPrefHeight(25.0);
        msgInput.setPrefWidth(226.0);
        msgInput.setPromptText("Enter message...");
        msgInput.setFont(new Font("Bookman Old Style", 12.0));
    
        msgSend = new Button("Send");
        msgSend.setLayoutX(262.0);
        msgSend.setLayoutY(435.0);
        msgSend.setFont(new Font("Bookman Old Style", 12.0));

        msgSend.setOnAction(e -> {
            String message = msgInput.getText();
            if (!message.isEmpty()) {
                appendMessage("You: " + message);
                sendMessage(message);
                msgInput.clear();
            }
        });
    
        pane.getChildren().addAll(msgArea, msgInput, msgSend);
        anchorPane.getChildren().addAll(chatAppBackground, lowerLeftCorner, upperRightCorner, upperLeftCorner, lowerRightCorner,
                leftBorder, rightBorder, topBorder, bottomBorder, pane);
        root.getChildren().add(anchorPane);
    
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}    
