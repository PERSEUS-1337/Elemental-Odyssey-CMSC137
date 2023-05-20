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
    private Integer serverPort;
    private String serverIP;
    private List<Socket> clientSockets = new ArrayList<>();
    private List<OutputStreamWriter> clientWriters = new ArrayList<>();

    private String chatName;
    public static final String SERVER = "Server";
    public static final String CLIENT = "Client";

    public ChatGUI(String chatType, String chatName, String ipAddress) {
        this.serverIP = ipAddress;
        this.serverPort = 5000;
        this.chatName = chatName;

        if (chatType.equals(SERVER)) {
            Thread serverThread = new Thread(this::startServer);
            serverThread.start();
            clientListen();
            // Set-up the GUI
            this.setStage(new Stage());
        } else if (chatType.equals(CLIENT) && isServerRunning()) {
            clientListen();
            // Set-up the GUI
            this.setStage(new Stage());
        }
    }


    // method for starting the server. It processes the client sockets and the client writers in separate threads, so that the server can handle multiple clients
    private void startServer() {
        try {
            InetAddress address = InetAddress.getByName(this.serverIP);
            this.server = new ServerSocket();
            this.server.bind(new InetSocketAddress(address, this.serverPort));
            System.out.println("Server instantiated at port " + this.serverPort);
            System.out.println("Waiting for client(s) to connect...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Client connected");

                clientSockets.add(clientSocket);

                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                OutputStreamWriter clientWriter = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8);

                // Add the client writer to the list
                clientWriters.add(clientWriter);

                // Thread for processing the client sockets' messages
                Thread receivingThread = new Thread(() -> {
                    try {
                        while (true) {
                            String message = reader.readLine();
                            if (message == null) {
                                break;
                            }
                            // Forward the message to all connected clients
                            sendMessageToAllClients(message, clientWriter);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        // Remove the client writer from the list when the client disconnects
                        clientWriters.remove(clientWriter);
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                receivingThread.start();
            }

        } catch (IOException e) {
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
    boolean isServerRunning() {
        try {
            InetAddress address = InetAddress.getByName(this.serverIP);
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(address, this.serverPort));
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
            System.out.println("Connected to server at port " + serverPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            // Thread for receiving messages from the server
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
                }
            });
            receivingThread.start();
        } catch (IOException e) {
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

    // method for closing the client sockets
    private void closeClientSockets() {
        for (Socket clientSocket : clientSockets) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // method to get the stage
    Stage getStage() {
        return this.stage;
    }

    // method to close the socket
    void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
            closeClientSockets();
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // method to set the Chat GUI stage components
    void setStage(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Chat App");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(328.0, 527.0);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(328.0, 530.0);

        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("../assets/background/chatBackground.png")));
        background.setFitHeight(530.0);
        background.setFitWidth(328.0);
        background.setPickOnBounds(true);

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
        anchorPane.getChildren().addAll(background, lowerLeftCorner, upperRightCorner, upperLeftCorner, lowerRightCorner,
                leftBorder, rightBorder, topBorder, bottomBorder, pane);
        root.getChildren().add(anchorPane);

        Scene scene = new Scene(root);
        this.stage.setScene(scene);
        this.stage.resizableProperty().setValue(Boolean.FALSE); // Disables the ability to resize the window
        this.stage.show();
    }
}
