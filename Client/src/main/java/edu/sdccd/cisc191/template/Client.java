package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public static User user = new User("Chase", 1000000);

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
        BorderPane borderPane = new BorderPane();


        Game[] response = new Game[]{
                new Game("Team 1", "Team 2", new Date(125, Calendar.APRIL, 24)),
                new Game("Team 5", "Team 6", new Date(125, Calendar.APRIL, 26)),
                new Game("Team 3", "Team 4", new Date(125, Calendar.APRIL, 25)),
                new Game("Team 7", "Team 8", new Date(125, Calendar.APRIL, 27))

        };

        User[] users = new User[]{
                new User("Andy", 129030),
                new User("Mahad", 214923),
                new User("Julian", 324960),
                new User("Brian", 492313),

        };



        VBox labelView = new VBox(10);
        HBox userInfo = new HBox(10);
        VBox betList = new VBox(10);
        VBox botsBox  = new VBox(10);

        botsBox.setPadding(new Insets(1));


        try {

            for (Game game : response) {
                HBox gameBox = new HBox(10); // Encapsulating HBox for all of the labels and buttons about a game

                HBox versusBox = new HBox(5); // Encapsulating all the labels (teams, date) about a game

                // Label for the first team
                Label team1 = new Label(game.getTeam1());
                team1.setTextFill(Color.color(1, 0, 0));
                team1.setStyle("-fx-font-weight: bold");

                Label vs = new Label("vs. ");

                // Label for the second team
                Label team2 = new Label(game.getTeam2());
                team2.setTextFill(Color.color(0, 0, 1));
                team2.setStyle("-fx-font-weight: bold");

                Locale loc = new Locale("en", "US");
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);
                String stringDate = dateFormat.format(game.getDate());

                // Label for the date
                Label date = new Label(stringDate);
                date.setTextFill(Color.rgb(117, 117, 117));

                // Place the team 1, team 2 and date labels into the label box
                gameBox.getChildren().addAll(team1, vs, team2, date);

                HBox betBox = new HBox(5); // Encapsulating HBox for Bet action buttons

                // Button to bet on the first team
                Button betTeam1 = new Button("Bet " + game.getTeam1());
                betTeam1.setOnAction(evt -> {
                    try {
                        new BetView().betView(stage, game);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                Label team1Odds = new Label(game.getTeam1Odd() + "%");

                // Button to bet on the second team
                Button betTeam2 = new Button("Bet " + game.getTeam2());
                betTeam2.setOnAction(evt -> {
                    try {
                        new BetView().betView(stage, game);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                for (Bet bet : user.getBets()) {
                    System.out.println("Checking bet: " + bet.getGame());
                    if (bet.getGame().equals(game)) {
                        System.out.println("Match found! Disabling buttons.");
                        betTeam1.setDisable(true);
                        betTeam2.setDisable(true);
                    }
                }
                Label team2Odds = new Label(game.getTeam2Odd() + "%");


                betBox.getChildren().addAll(betTeam1, team1Odds, betTeam2, team2Odds); // Add the bet buttons to the encapsulating HBox

                gameBox.getChildren().addAll(versusBox, betBox); // Add everything involving the game (labels, bet buttons) to a row

                labelView.getChildren().add(gameBox); // Add new row to the list of upcoming games
            }

            userInfo.setBackground(Background.fill(Color.rgb(45, 51, 107)));
            Label userName = new Label(user.getName());
            userName.setFont(new Font(20));
            userName.setTextFill(Color.WHITE);

            Label money = new Label("$" + user.getMoney());
            money.setFont(new Font(20));
            money.setTextFill(Color.WHITE);



            for (User user : users) {
                HBox botBox = new HBox(10);
                Label userName1 = new Label(user.getName());
                Label money1 = new Label("$" + user.getMoney());
                botBox.getChildren().addAll(userName1, money1);
                botsBox.getChildren().add(botBox);
            }


            for (Bet bet : user.getBets()) {
                HBox betBox = new HBox(10);
                Label game = new Label(bet.getGame().toString());
                Label betAmt = new Label("Bet $" + bet.getBetAmt());
                Label winAmt = new Label("Win $ " + bet.getWinAmt());
                betBox.getChildren().addAll(game, betAmt, winAmt);
                betList.getChildren().add(betBox);
            }

            userInfo.getChildren().addAll(userName, money);
        } catch (Exception e) {
            e.printStackTrace();
        }

        labelView.setPadding(new Insets(20));


        borderPane.setCenter(labelView);
        borderPane.setBottom(betList);
        borderPane.setTop(userInfo);
        borderPane.setRight(botsBox);

        Scene scene = new Scene(borderPane, 1200, 600);
        stage.setScene(scene);
        stage.setTitle("Marauder Bets");
        stage.show();


    }
} //end class Client

