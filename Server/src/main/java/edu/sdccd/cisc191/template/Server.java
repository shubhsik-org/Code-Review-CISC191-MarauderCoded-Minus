package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;

/**
 * A multi-threaded server that listens for connection requests on a specified port
 * and handles each client connection in a separate thread.
 *
 * The server sends the current time to connected clients and continues running
 * indefinitely until it is terminated manually. It uses the {@link ClientHandler}
 * class to process individual client connections.
 *
 * @version 1.0.1
 * @author Andy Ly
 * @see ClientHandler
 */
public class Server {
    private static boolean running = true; // Control flag for stopping the server

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            // Initialize the server to listen on port 4442 with a backlog of 4096.
            serverSocket = new ServerSocket(4444, 4096);
            System.out.println("Server started on port 4444");

            // Enable address reuse to avoid "Address already in use" errors.
            serverSocket.setReuseAddress(true);

            while (running) {
                try {
                    // Accept client connections
                    Socket client = serverSocket.accept();
                    System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                    // Handle the client connection in a separate thread
                    ClientHandler clientSocket = new ClientHandler(client);
                    new Thread(clientSocket).start();
                } catch (SocketException e) {
                    // This happens when the server is forcefully stopped
                    System.out.println("Server socket closed, stopping server...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Ensure the server socket is closed when the server terminates
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Server socket on port 4444 closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Stops the server by setting the `running` flag to false
     * and closing the server socket.
     */
    public static void stopServer() {
        running = false;
        try {
            new Socket("localhost", 4444).close(); // Force unblock accept()
        } catch (IOException ignored) {
        }
    }
}
