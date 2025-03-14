package edu.sdccd.cisc191.template;

import java.net.*;
import java.io.*;
import java.util.Arrays;
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
        while ((inputLine = in.readLine()) != null) {
            CustomerRequest request = CustomerRequest.fromJSON(inputLine);
            System.out.println(request.toString());
            Game game1 = new Game("Team 1", "Team 2", new Date(2025, 2, 24));
            Game game2 = new Game("Team 3", "Team 4", new Date(2025, 2, 25));
            Game game3 = new Game("Team 5", "Team 6", new Date(2025, 3, 26));

            Game response = game1;
            out.println(Game.toJSON(response));
            System.out.println(response);
            System.out.println(Game.toJSON(response));
            System.out.println(response.getClass());
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(4444);
            server.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
} //end class Server
