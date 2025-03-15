package edu.sdccd.cisc191.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// Thread to handle clients
class ClientHandler implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    // Discerns the type of client and then passes it on to the corresponding handler function
    public void run() {

        System.out.println("Passed duties on to ClientHandler...");

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                CustomerRequest request = CustomerRequest.fromJSON(inputLine);
                System.out.println(request.toString());

                if(Objects.equals(request.getRequestType(), "Game")) {
                    Game response = null;
                    if (request.getId() >= 0) {
                        response = getGame(request);
                    }
                    out.println(Game.toJSON(response));
                }

                else if (Objects.equals(request.getRequestType(), "User")) {
                    User response = null;
                    if(request.getId() >= 0) {
                        response = getUser(request);
                    }
                    out.println(User.toJSON(response));
                }
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

//BEGIN HANDLER FUNCTIONS

    // Gets the corresponding game that was requested
    private static Game getGame(CustomerRequest request) {
        Game response;

        ArrayList<Game> gameDatabase= new ArrayList<Game>();
        // Populate the gameDatabase with fake data
        int count = 0; // To generate real looking data
        int i = 0;

        // Enter desired number of elements here
        while (i < 5) {
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

    // Gets the corresponding User that was requested
    private static User getUser(CustomerRequest request) {
        User response;

        ArrayList<User> userDatabase= new ArrayList<User>();
        // Populate the userDatabase with fake data
        int i = 0;
        // Enter desired number of elements here
        while (i < 5) {
            userDatabase.add(new User(UUID.randomUUID().toString().replace("-", "").substring(0, 10), (int) (Math.random() * 1000)));
            i += 1;
        }

        // Check if the ID is a valid User, then return
        if (request.getId() >= userDatabase.size()) {
            response = null;
        } else {
            response = userDatabase.get(request.getId());
        }
        return response;
    }
}
