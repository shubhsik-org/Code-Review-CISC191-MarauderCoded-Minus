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
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void startConnection(String ip, int port) throws IOException {
//        clientSocket = new Socket(ip, port);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    }
//
//    public CustomerResponse sendRequest() throws Exception {
//        out.println(CustomerRequest.toJSON(new CustomerRequest(1)));
//        return CustomerResponse.fromJSON(in.readLine());
//    }
//
//    public void stopConnection() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//    }
//    public static void main(String[] args) {
//        Client client = new Client();
//        try {
//            client.startConnection("127.0.0.1", 4444);
//            System.out.println(client.sendRequest().toString());
//            client.stopConnection();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        launch();  // Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {

        Label message = new Label("First FX Application!");
        message.setFont(new Font(40));

        Button helloButton = new Button("Say Hello");
        helloButton.setOnAction(evt -> message.setText("Hello World!"));
        Button goodbyeButton = new Button("Say Goodbye");
        goodbyeButton.setOnAction( evt -> message.setText("Goodbye!!") );
        Button quitButton = new Button("Quit");
        quitButton.setOnAction( evt -> Platform.exit() );

        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(helloButton, goodbyeButton, quitButton);
        buttonBar.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        root.setCenter(message);
        root.setBottom(buttonBar);

        Scene scene = new Scene(root, 450, 200);
        stage.setScene(scene);
        stage.setTitle("JavaFX Test");
        stage.show();


    }
} //end class Client

