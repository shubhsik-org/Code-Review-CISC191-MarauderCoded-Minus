package edu.sdccd.cisc191.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

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
            // Server logic goes in here(as in processing the type of request received
            // Positive IDs correspond to a getGame request
            while ((inputLine = in.readLine()) != null) {
                CustomerRequest request = CustomerRequest.fromJSON(inputLine);
                System.out.println(request.toString());

                Game response = null;

                if(request.getId() >= 0) {
                    response = getGame(request);
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

    // Gets the corresponding game that was requested
    private static Game getGame(CustomerRequest request) {
        Game response;

        ArrayList<Game> gameDatabase= new ArrayList<Game>();
        // Populate the gameDatabase with fake data
        int count = 0; // To generate real looking data
        int i = 0;

        // Enter desired number of elements here
        while (i < 10) {
            gameDatabase.add(new Game(String.format("Team %d", count), String.format("Team %d", count + 1), new Date(2025 + count, count % 12, count % 12)));
            count += 2;
            i += 1;
        }

        // Check if the ID is a valid game, then return
        if (request.getId() >= gameDatabase.size()) {
            response = null;
        } else {
            response = gameDatabase.get(request.getId());
        }
        return response;
    }
}
