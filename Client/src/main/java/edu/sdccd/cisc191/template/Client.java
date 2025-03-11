package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
import java.util.Date;

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
//    public Game[] sendRequest() throws Exception {
//        out.println(CustomerRequest.toJSON(new CustomerRequest(1)));
//        return new Game[]{Game.fromJSON(in.readLine())};
//    }
//
//    public void stopConnection() throws IOException {
//        in.close();
//        out.close();
//        clientSocket.close();
//    }
//    public Game[] accessServer() {
//        Client client = new Client();
//        try {
//            client.startConnection("127.0.0.1", 4444);
//            return client.sendRequest();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void main(String[] args) {
        launch();  // Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {
        Game[] response = new Game[]{
                new Game("Team 1", "Team 2", new Date(2025, 2, 24)),
                new Game("Team 3", "Team 4", new Date(2025, 2, 25)),
                new Game("Team 5", "Team 6", new Date(2025, 2, 26)),
                new Game("Team 7", "Team 8", new Date(2025, 2, 27))

        };


        VBox labelView = new VBox(10);
        HBox userInfo = new HBox(10);
        VBox betList = new VBox(10);

        User user = new User("Chase", 1000000);


        // Mock response
            // Iterate over the response and create new Labels
        try {
            for (Game game : response) {
                HBox gameBox = new HBox(10); // Encapsulating HBox for all of the information about a game

                // Label for the first team
                Label team1 = new Label(game.getTeam1());
                team1.setTextFill(Color.color(1, 0, 0));
                team1.setStyle("-fx-font-weight: bold");

                Label vs = new Label("vs. ");

                Label team2 = new Label(game.getTeam2());
                team2.setTextFill(Color.color(0, 0, 1));
                team2.setStyle("-fx-font-weight: bold");

                HBox versusBox = new HBox(5);
                Label date = new Label(game.getDate().getMonth() + "/" + game.getDate().getDay() + "/" + game.getDate().getYear());
                date.setTextFill(Color.rgb(117, 117, 117));
                versusBox.getChildren().addAll(team1, vs, team2, date);

                Button betTeam1 = new Button("Bet " + game.getTeam1());
                betTeam1.setOnAction(evt -> {
//                    Dialog<String> dialog = new Dialog<>();
//                    dialog.setTitle("Bet Dialog");
//                    dialog.setHeaderText("Place your bet for " + game.getTeam1());
//                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//                    dialog.showAndWait();
                    try {
                        new BetView().betView(user, stage, game);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });
                Button betTeam2 = new Button("Bet " + game.getTeam2());
                betTeam2.setOnAction(evt -> {
//                    Dialog<String> dialog = new Dialog<>();
//                    dialog.setTitle("Bet Dialog");
//                    dialog.setHeaderText("Place your bet for " + game.getTeam2());
//
//                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//
//                    dialog.showAndWait();
                    try {
                        new BetView().betView(user, stage, game);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });


                HBox betBox = new HBox(5);
                betBox.getChildren().addAll(betTeam1, betTeam2);

                gameBox.getChildren().addAll(versusBox, betBox);

                labelView.getChildren().add(gameBox); // Add the new Label
            }

            userInfo.setBackground(Background.fill(Color.color(0, 1, 0)));
            Label userName = new Label(user.getName());

            Label money = new Label("" + user.getMoney());


            for (Bet bet : user.getBets()) {
                HBox betBox = new HBox(10);
                Label game = new Label(bet.getGame().toString());
                Label betAmt = new Label(bet.getBetAmt() + "");
                Label winAmt = new Label(bet.getWinAmt() + "");
                betBox.getChildren().addAll(game, betAmt, winAmt);
                betList.getChildren().add(betBox);
            }

            userInfo.getChildren().addAll(userName, money);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Button helloButton = new Button("Vs");
//        helloButton.setOnAction(evt -> {
//            msg1.setText(response[0].getTeam1() + " Vs " + response[0].getTeam2());
//            msg2.setText(response[1].getTeam1() + " Vs " + response[1].getTeam2());
//            msg3.setText(response[2].getTeam1() + " Vs " + response[2].getTeam2());
//                });Ω
//        Button goodbyeButton = new Button("Date");
//        goodbyeButton.setOnAction(evt -> {
//            msg1.setText(response[0].getDate().toString());
//            msg2.setText(response[0].getDate().toString());
//            msg3.setText(response[0].getDate().toString());
//        });
//        Button quitButton = new Button("Quit");
//        quitButton.setOnAction( evt -> Platform.exit() );

        labelView.setAlignment(Pos.CENTER);
//        root.setBottom(buttonBar);

        VBox everything = new VBox(10);
        everything.getChildren().addAll(userInfo, labelView, betList);

        Scene scene = new Scene(everything, 450, 200);
        stage.setScene(scene);
        stage.setTitle("Marauder Bets");
        stage.show();


    }
} //end class Client

