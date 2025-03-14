package edu.sdccd.cisc191.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

// ClientHandler class
class ClientHandler implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        System.out.println("Passed duties on to ClientHandler...");
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
