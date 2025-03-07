package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

public class Client extends Application {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public Game[] sendRequest() throws Exception {
        out.println(CustomerRequest.toJSON(new CustomerRequest(1)));
        return new Game[]{Game.fromJSON(in.readLine())};
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
    public Game[] accessServer() {
        Client client = new Client();
        try {
            client.startConnection("127.0.0.1", 4444);
            return client.sendRequest();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch();  // Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {

        Game[] response = accessServer();

        Label msg1 = new Label("Betting App");
        Label msg2 = new Label("Betting App");
        Label msg3 = new Label("Betting App");

        msg1.setFont(new Font(40));
        msg2.setFont(new Font(40));
        msg3.setFont(new Font(40));


        Button helloButton = new Button("Vs");
        helloButton.setOnAction(evt -> {
            msg1.setText(response[0].getTeam1() + " Vs " + response[0].getTeam2());
            msg2.setText(response[1].getTeam1() + " Vs " + response[1].getTeam2());
            msg3.setText(response[2].getTeam1() + " Vs " + response[2].getTeam2());
                });
        Button goodbyeButton = new Button("Date");
        goodbyeButton.setOnAction(evt -> {
            msg1.setText(response[0].getDate().toString());
            msg2.setText(response[0].getDate().toString());
            msg3.setText(response[0].getDate().toString());
        });
        Button quitButton = new Button("Quit");
        quitButton.setOnAction( evt -> Platform.exit() );

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(helloButton, goodbyeButton, quitButton);
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setTop(msg1);
        root.setCenter(msg2);
        root.setLeft(msg3);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 450, 200);
        stage.setScene(scene);
        stage.setTitle("JavaFX Test");
        stage.show();


    }
} //end class Client

