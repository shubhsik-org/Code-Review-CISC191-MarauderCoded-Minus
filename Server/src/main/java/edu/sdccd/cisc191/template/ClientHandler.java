package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    // Discerns the type of clientRequest and then passes it on to the corresponding handler function
    public void run() {
        
        System.out.println("Passed duties on to ClientHandler...");
        
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                CustomerRequest request = CustomerRequest.fromJSON(inputLine);
                System.out.println(request.toString());

                //BEGIN REQUEST DISCERNMENT
                // Handles gameGetRequest calls
                if(Objects.equals(request.getRequestType(), "Game")) {
                    Game response = null;
                    if (request.getId() >= 0) {
                        response = getGame(request);
                    }
                    out.println(Game.toJSON(response));
                } 

                // Handles userGetRequest calls
                else if (Objects.equals(request.getRequestType(), "User")) {
                    User response = null;
                    if(request.getId() >= 0) {
                        response = getUser(request);
                    }
                    out.println(User.toJSON(response));
                }

                // Handle ModifyUser request
                else if (Objects.equals(request.getRequestType(), "ModifyUser")) {
                    User response = null;
                    if (request.getId() >= 0) {
                       response = handleModifyUserRequest(request);
                    }
                    out.println(User.toJSON(response));
                }
            } // END REQUEST DISCERNMENT
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

//BEGIN DATABASE FUNCTIONS

    private static ArrayList<Game> populateGameDatabase(int numberOfGames) {
    ArrayList<Game> gameDatabase= new ArrayList<Game>();
    // Populate the gameDatabase with fake data
    int count = 0; // To generate real looking data

    // Enter desired number of elements here
        for(int i = 0; i < numberOfGames; i++) {
            gameDatabase.add(new Game(String.format("Team %d", count), String.format("Team %d", count + 1), new Date(2025 + count, count % 12, count % 12)));
            count += 2;
        }
        return gameDatabase;
    }



//BEGIN HANDLER FUNCTIONS
    
    // getGameRequest Handler
    private static Game getGame(CustomerRequest request) {
        Game response;

        ArrayList<Game> gameDatabase = populateGameDatabase(5);
        // Check if the ID is a valid game, then return
        if (request.getId() >= gameDatabase.size()) {
            response = null;
        } else {
            response = gameDatabase.get(request.getId());
        }
        return response;
    }

    // getUserRequest Handler
    private static User getUser(CustomerRequest request) {
        User response;

        List<User> userDatabase = UserDatabase.getUserDatabase();
        if (request.getId() >= userDatabase.size()) {
            response = null;
        } else {
            response = userDatabase.get(request.getId());
        }
        return response;
    }

    private static User handleModifyUserRequest(CustomerRequest request) throws Exception {

        User response;
        List<User> userDatabase = UserDatabase.getUserDatabase();

        // Locate the user
        User userToModify = userDatabase.get(request.getId());

        // Update Name or Money
        Map<String, Object> attributes = request.getAttributesToModify();
        if (attributes.containsKey("Name")) {
            userToModify.setName((String) attributes.get("Name"));
        }
        if (attributes.containsKey("Money")) {
            userToModify.setMoney((Integer) attributes.get("Money"));
        }
        // Update Bets by value
        if (attributes.containsKey("addBet")) {
            // De-serialize bet object so we can do modifications to it
            userToModify.addBet(Bet.fromJSON((String) attributes.get("addBet")));
        }
        if (attributes.containsKey("removeBet")) {
            // De-serialize bet object so we can do modifications to it
            userToModify.removeBet(Bet.fromJSON((String) attributes.get("removeBet"))); // Removing a game by ID
        }

        // Return the updated user
        return userToModify;

    }
}
