package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;
import java.util.Date;

/**
 * This program is a server that takes connection requests on
 * the port specified by the constant LISTENING_PORT.  When a
 * connection is opened, the program sends the current time to
 * the connected socket.  The program will continue to receive
 * and process connections until it is killed (by a CONTROL-C,
 * for example).  Note that this server processes each connection
 * as it is received, rather than creating a separate thread
 * to process the connection.
 */
public class Server {
    public static void main(String[] args) {
        // In case we cannot later establish a server for some reason
        // This declaration will allow us to exit gracefully.
        ServerSocket serverSocket = null;

        try {

            // Server is listening on port 4444
            serverSocket = new ServerSocket(4444, 4096);
            System.out.println("Server started on port 4444");

            // Allows the multi-threading to happen
            serverSocket.setReuseAddress(true);

            // Wait for clients forever
            while (true) {

                Socket client = serverSocket.accept();

                // Displaying that new client is connected to the server
                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                // Create the thread we pass on handling to
                ClientHandler clientSocket = new ClientHandler(client);


                new Thread(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
//        Server server = new Server();
//        try {
//            server.start(4444);
//            server.stop();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
