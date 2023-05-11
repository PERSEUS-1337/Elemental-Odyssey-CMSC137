package chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clients; // Add a list of connected clients

    // Constructor with the desired port number
    public Server(int port) {
        this.port = port;
        this.clients = Collections.synchronizedList(new ArrayList<>()); // Initialize the list
    }

    // Method to start the server
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                // Create a new ClientHandler for the connected client    
                ClientHandler handler = new ClientHandler(clientSocket, this);
                clients.add(handler); // Add the new ClientHandler to the list
    
                Thread clientThread = new Thread(handler);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    // ClientHandler class to handle clients using multithreading
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private DataInputStream input;
        private DataOutputStream output;
        private Server server; // Add a reference to the server

        public ClientHandler(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.server = server; // Set the server reference
        }

        @Override
        public void run() {
            try {
                // Obtain the input and output streams
                input = new DataInputStream(clientSocket.getInputStream());
                output = new DataOutputStream(clientSocket.getOutputStream());

                // Read the client's message and reply
                String clientMessage = input.readUTF();
                System.out.println("Client says: " + clientMessage);

                String serverMessage = "Client says: " + clientMessage;
                broadcast(serverMessage);
                System.out.println("Server replied: " + serverMessage);

                // Close the connection
                input.close();
                output.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }

        private void broadcast(String message) {
            synchronized (server.clients) {
                for (ClientHandler client : server.clients) {
                    if (client != this) {
                        try {
                            client.output.writeUTF(message);
                        } catch (IOException e) {
                            System.err.println("Error broadcasting message: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        // Instantiate the server with a specific port number
        Server server = new Server(5000);

        // Start the server
        server.startServer();
    }
}
