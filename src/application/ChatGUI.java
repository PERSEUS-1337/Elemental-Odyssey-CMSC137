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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatGUI {
    // GUI Variables for Chat App
    private Stage stage;
    private TextArea msgArea;
    private TextField msgInput;
    private Button msgSend;

    // Socket Programming Variables for Chat
    private Socket socket;
    private ServerSocket server;
    private OutputStreamWriter writer;
    private String serverIP;
    private List<Socket> clientSockets = new ArrayList<>();
    private List<OutputStreamWriter> clientWriters = new ArrayList<>();
    private Thread serverThread;
    private Thread clientThread;

    private String chatName;
    private String chatType;
    private static Integer serverPort = 54321;      // Port number for the chat server
    public static final String SERVER = "Server";
    public static final String CLIENT = "Client";
    public static final String DEFAULT_SERVER = "Connect to Me";

    public ChatGUI(String chatType, String chatName, String ipAddress, Stage stage) {
        
        this.serverIP = ipAddress;
        this.chatName = chatName;
        this.chatType = chatType;

        if (this.chatType.equals(SERVER)) {
            if (isServerRunning(this.serverIP)){
                System.out.println("Chat: Server is already running!");
                return;  
            } else {
                serverThread = new Thread(this::startServer);
                serverThread.start();
                clientThread = new Thread(this::clientListen);
                clientThread.start();
                // Set-up the GUI
                this.setStage(stage);
            }
        } else if (this.chatType.equals(CLIENT)) {
            if (isServerRunning(this.serverIP)){
                clientThread = new Thread(this::clientListen);
                clientThread.start();
                // Set-up the GUI
                this.setStage(stage);
            } else {
                System.out.println("Chat: Server is not running yet");
                return;
            }
        } 
    }


    // method for starting the server. It processes the client sockets and the client writers in separate threads, so that the server can handle multiple clients
    private void startServer() {
        try {
            this.server = new ServerSocket();
            this.server.setReuseAddress(true);
            this.server.bind(new InetSocketAddress(serverPort));
            System.out.println("Chat: Waiting for client(s) to connect...");

            while (!this.server.isClosed()) {
                Socket clientSocket = server.accept();
                System.out.println("Chat: Client connected");

                clientSockets.add(clientSocket);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                OutputStreamWriter clientWriter = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);

                // Add the client writer to the list
                clientWriters.add(clientWriter);

                // Thread for processing the client sockets' messages
                Thread receivingThread = new Thread(() -> {
                    try {
                        while (clientSocket.isConnected()) {
                            String message = reader.readLine();
                            if (message == null) {
                                break;
                            }
                            // Forward the message to all connected clients
                            sendMessageToAllClients(message, clientWriter);
                        }
                    } catch (Exception e) {
                        if (!(e instanceof SocketException))
                            e.printStackTrace();
                    } finally {
                        // Remove the client writer from the list when the client disconnects
                        clientWriters.remove(clientWriter);
                    }
                });
                receivingThread.start();
            }

        } catch (Exception e) {
            if (!(e instanceof SocketException))
                e.printStackTrace();
        }
    }

    // method for sending message to all clients except the sender
    private void sendMessageToAllClients(String message, OutputStreamWriter senderWriter) {
        for (OutputStreamWriter clientWriter : clientWriters) {
            if (clientWriter != senderWriter) {
                try {
                    clientWriter.write(message + "\n");
                    clientWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // method to check if the server socket address is already running
    static boolean isServerRunning(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(address, serverPort));
            server.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    // method for listening to the server
    private void clientListen() {
        try {
            socket = new Socket(this.serverIP, serverPort);
            System.out.println("Chat: Connected to server at port " + serverPort + " and IP Address " + this.serverIP);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            // Thread for receiving messages from the server
            Thread receivingThread = new Thread(() -> {
                try {
                    while (socket.isConnected()) {
                        String message = reader.readLine();
                        if (message == null) {
                            break;
                        }
                        appendMessage(message);
                    }
                } catch (Exception e) {
                    if (!(e instanceof SocketException))
                        e.printStackTrace();
                } finally {
                    // try {
                    //     socket.close();
                    // } catch (IOException e) {
                    //     e.printStackTrace();
                    // }
                }
            });
            receivingThread.start();
        } catch (Exception e) {
            if (!(e instanceof SocketException))
                e.printStackTrace();
        }
    }

    // method for sending message to the server
    private void sendMessage(String message) {
        try {
            writer.write(this.chatName + ": " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method for appending message to the message area
    private void appendMessage(String message) {
        msgArea.appendText(message + "\n");
    }

    // method to get the stage
    Stage getStage() {
        return this.stage;
    }

    // method to close the socket
    // void closeSocket() {
    //     System.out.println("Chat: Closing socket...");

    //     if (this.chatType.equals(SERVER)) {
    //         try {
    //             this.socket.close();
    //             this.server.close();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     } else if (this.chatType.equals(CLIENT)) {
    //         try {
    //             this.socket.close();
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    void closeChatServer(){
        System.out.println("Chat: Closing server socket");
        try{
            this.server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeChatClient(){
        System.out.println("Chat: Closing client socket");
        try{
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to set the Chat GUI stage components
    private void setStage(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("User: " + this.chatName);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(328.0, 527.0);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(328.0, 530.0);

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("../assets/background/chatBackground.png")));
        background.setFitHeight(530.0);
        background.setFitWidth(328.0);
        background.setPickOnBounds(true);

        ImageView lowerLeftCorner = createImageView("../assets/blocks/lowerLeftCorner_Gold.png", 34.0, 23.0, 0.0, 504.0);

        ImageView upperRightCorner = createImageView("../assets/blocks/upperRightCorner_Gold.png", 31.0, 23.0, 297.0, -1.0);

        ImageView upperLeftCorner = createImageView("../assets/blocks/upperLeftCorner_Gold.png", 31.0, 35.0, 0.0, 0.0);

        ImageView lowerRightCorner = createImageView("../assets/blocks/lowerRightCorner_Gold.png", 31.0, 23.0, 297.0, 504.0);

        ImageView leftBorder = createImageView("../assets/blocks/longVerticalGoldBorder.png", 51.0, 481.0, 0.0, 23.0);

        ImageView rightBorder = createImageView("../assets/blocks/longVerticalGoldBorder.png", 51.0, 481.0, 323.0, 23.0);

        ImageView topBorder = createImageView("../assets/blocks/longHorizontalGoldBorder.png", 269.0, 7.0, 30.0, 0.0);

        ImageView bottomBorder = createImageView("../assets/blocks/longHorizontalGoldBorder.png", 269.0, 7.0, 30.0, 524.0);

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
        msgArea.setFont(new Font("Bookman Old Style", 12.0));

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
        anchorPane.getChildren().addAll(background, lowerLeftCorner, upperRightCorner, upperLeftCorner, lowerRightCorner,
                leftBorder, rightBorder, topBorder, bottomBorder, pane);
        root.getChildren().add(anchorPane);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
        this.stage.show();
    }

    // method to set an ImageView component
    private ImageView createImageView(String imageUrl, double fitWidth, double fitHeight, double layoutX, double layoutY) {
        Image image = new Image(getClass().getResourceAsStream(imageUrl));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
