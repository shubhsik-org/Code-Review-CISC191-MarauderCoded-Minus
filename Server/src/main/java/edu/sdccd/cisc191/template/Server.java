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
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws Exception {
        System.out.println("Starting server on port " + port);
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        //Server logic goes here...
        // Inputline is a stream of CustomerRequest objects in
        // JSON format
        while ((inputLine = in.readLine()) != null) {
            CustomerRequest request = CustomerRequest.fromJSON(inputLine);
            System.out.println(request.toString());
            Game response;

            Game[] gameDatabase = new Game[]{
                    new Game("Team 1", "Team 2", new Date(2025, 2, 24)),
                    new Game("Team 3", "Team 4", new Date(2025, 2, 25)),
                    new Game("Team 5", "Team 6", new Date(2025, 3, 26)),
            };
            if (request.getId() >= gameDatabase.length) {
                response = null;
            } else {
                response = gameDatabase[request.getId()];
            }
            out.println(Game.toJSON(response));
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {

            // server is listening on port 4444
            serverSocket = new ServerSocket(4444);

            // Allows the multi-threading to happen
            serverSocket.setReuseAddress(true);

            // Wait for clients forever
            while (true) {

                // socket object to receive incoming client
                // requests
                Socket client = serverSocket.accept();

                // Displaying that new client is connected
                // to server
                System.out.println("New client connected: "
                        + client.getInetAddress()
                        .getHostAddress());

                // create a new thread object
                ClientHandler clientSocket
                        = new ClientHandler(client);

                // This thread will handle the client
                // separately
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
