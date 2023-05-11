package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import javafx.scene.control.TextArea;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.concurrent.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    private TextArea messageArea;
    private TextField inputField;
    private PrintWriter out;
	public VBox layout;

	public static void main(String[] args) {
		Client chatClient = new Client();
		chatClient.initialize();
		// chatClient.initialize(new Stage());
	}
	

    public void initialize() {
		// public void initialize(Stage primaryStage) {
			// messageArea = new TextArea();
        // messageArea.setEditable(false);
        // messageArea.setPrefSize(400, 300);

        // inputField = new TextField();
        // inputField.setOnAction(e -> {
        //     out.println(inputField.getText());
        //     inputField.clear();
        // });

        // this.layout = new VBox(5, messageArea, inputField);
        // Scene scene = new Scene(this.layout);

        // primaryStage.setTitle("In-Game Chat");
        // primaryStage.setScene(scene);
        // primaryStage.show();
        System.out.println("Client Attempting to Connect to Server");
        connectToServer();
		
    }

	private void connectToServer() {
		Socket socket = null;
		try {
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
	
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(() -> {
				try {
					String message;
					while ((message = in.readLine()) != null) {
						messageArea.appendText(message + "\n");
					}
				} catch (IOException e) {
					System.err.println("Error reading from server");
				}
			});
		} catch (IOException e) {
			System.err.println("Could not connect to server");
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.err.println("Error closing socket");
				}
			}
		}
	}
	

	public VBox getLayout() {
		return layout;
	}
	

	
}

