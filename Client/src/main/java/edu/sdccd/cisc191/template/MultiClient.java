package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

/**
 * This program opens a connection to a computer specified
 * as the first command-line argument.  If no command-line
 * argument is given, it prompts the user for a computer
 * to connect to.  The connection is made to
 * the port specified by LISTENING_PORT.  The program reads one
 * line of text from the connection and then closes the
 * connection.  It displays the text that it read on
 * standard output.  This program is meant to be used with
 * the server program, DateServer, which sends the current
 * date and time on the computer where the server is running.
 */

public class MultiClient extends Application {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    // Sends a request to the server.
    // for Game type requests, the ID specifies the index of the game in GameDatabase(TBD)
    // for User type requests, the ID specifies the index of the User in UserDatabase(TBD)
    public Game sendRequest(String requestType, int id) throws Exception {

        Client client = new Client();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending gameGetRequest");
            out.println(CustomerRequest.toJSON(new CustomerRequest(requestType, id)));
            return Game.fromJSON(in.readLine());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) {
        launch();// Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {
        MultiClient client = new MultiClient();
        int numClients = 10;
        for (int i = 0; i < numClients; i++) {
            new ClientThread(i).start();
        }
    }
}

class ClientThread extends Thread  {
    private int clientNumber;

    public ClientThread(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public void run() {

        MultiClient client = new MultiClient();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending request from client: "+ clientNumber);
            client.sendRequest("Game", this.clientNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}