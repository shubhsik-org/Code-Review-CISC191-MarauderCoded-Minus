package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import javax.security.auth.callback.Callback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.*;

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

    // Do not use this method directly to ensure you get a proper response back
    // Valid request types: "Game", "User", "GetSize"
    // returnType parameter is just the expected return type
    private <T> T sendRequest(String requestType, int id, Class<T> returnType) throws Exception {
        out.println(CustomerRequest.toJSON(new CustomerRequest(requestType, id)));
        String response = in.readLine();

        if (returnType == Game.class) {
            return returnType.cast(Game.fromJSON(response));
        } else if (returnType == User.class) {
            return returnType.cast(User.fromJSON(response));
        } else if (returnType == Integer.class) {
            return returnType.cast(Integer.parseInt(response));
        }
        else {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    // Overload sendRequest method to handle modify requests.
    // Valid request types: "User"
    // Should only be on user because we run CRUD on GameDatabase through the server
    private <T> T sendRequest(String requestType, int id, Map<String, Object> modifiedAttributes, Class<T> returnType) throws Exception {
        out.println(CustomerRequest.toJSON(new CustomerRequest(requestType, id, modifiedAttributes)));
        String response = in.readLine();

        if (returnType == Game.class) {
            return returnType.cast(Game.fromJSON(response));
        } else if (returnType == User.class) {
            return returnType.cast(User.fromJSON(response));
        } else {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    // Gets game with specified ID from database
    public Game gameGetRequest(int id) throws IOException {
        Client client = new Client();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending gameGetRequest with ID: " + id);
            return client.sendRequest("Game", id, Game.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        stopConnection();
        return null;
    }

    // Gets User with specified ID from database
    public User userGetRequest(int id) throws IOException {
        Client client = new Client();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending userRequest with ID: " + id);
            return client.sendRequest("User", id, User.class);
        } catch(Exception e) {
            e.printStackTrace();
        }

        stopConnection();
        return null;
    }

    // ID of 2 for userDatabase size and (TODO) ID of 1 for gameDatabase size
    public int getSizeRequest(int id) throws IOException {

        Client client = new Client();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending getSizeRequest with ID: " + id);
            return client.sendRequest("GetSize", id, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stopConnection();
        return -1;
    };

    // Modifies a User with specified ID from the database
    // Takes a map object with the key being the field of the User and the value being the modded value
    // Valid Keys to use in the Map "Name", "Money", "addBet" "removeBet"
    public User userModifyRequest(int id, Map<String, Object> modifiedAttributes) throws IOException {
        Client client = new Client();
        try {
            client.startConnection("localhost", 4444);
            System.out.println("Sending userModifyRequest with ID: " + id);
            return client.sendRequest("ModifyUser", id, modifiedAttributes, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopConnection();
        return null;
    }


    public static void main(String[] args) {
        launch();// Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();

        // TODO: update this so it can just pull all of the games at once, because what if there are more than 5 upcoming games? either that or have a server method that's the number of upcoming games, then update the assignment of this array to be a for loop that counts up and calls gameGetRequest
        Game[] response = new Game[]{
                gameGetRequest(0),
                gameGetRequest(1),
                gameGetRequest(2),
                gameGetRequest(3),
                gameGetRequest(4),
        };

        // TODO: see comment with Game[] response
        User[] users = new User[]{
                userGetRequest(0),
                userGetRequest(1),
                userGetRequest(2),
                userGetRequest(3),
                userGetRequest(4),
        };
/* BEGIN EXAMPLES OF HOW TO USE THE SERVER
        // Test modification of user
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("Name", "John");
        attributes.put("Money", 9999);
        // Serialize Bet object into JSON string before sending to server
        attributes.put("addBet", Bet.toJSON(new Bet(response[0], 100)));
        System.out.println(userModifyRequest(2, attributes));

        System.out.println(getSizeRequest(2));

        System.out.println(getSizeRequest(1));
        END EXAMPLES OF HOW TO USE THE SERVER
 */

        // BEGIN CLIENT VIEW CODE
        VBox labelView = new VBox(10);
        HBox userInfo = new HBox(10);
        HBox betList = new HBox(10);
        betList.setPrefHeight(200);


        TableView tableView = new TableView();

        // Describes a table with the games displayed
        TableColumn<Game, String> team1 = new TableColumn<>("Team 1");
        team1.setCellValueFactory(new PropertyValueFactory<>("team1"));
        tableView.getColumns().add(team1);
        team1.setResizable(false);
        team1.setSortable(false);
        team1.setReorderable(false);

        TableColumn<Game, String> team2 = new TableColumn<>("Team 2");
        team2.setCellValueFactory(new PropertyValueFactory<>("team2"));
        tableView.getColumns().add(team2);
        team2.setResizable(false);
        team2.setSortable(false);
        team2.setReorderable(false);


        TableColumn<String, String> startDate = new TableColumn<>("Start Date");
        startDate.setPrefWidth(500);
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tableView.getColumns().add(startDate);
        startDate.setResizable(false);
        startDate.setReorderable(false);
        startDate.setSortable(false);
        startDate.setPrefWidth(250);

        TableColumn<String, String> endDate = new TableColumn<>("End Date");
        endDate.setPrefWidth(500);
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tableView.getColumns().add(endDate);
        endDate.setResizable(false);
        endDate.setReorderable(false);
        endDate.setSortable(false);
        endDate.setPrefWidth(250);

        TableColumn<String, String> team1Odd = new TableColumn<>("Team 1 Odds");
        team1Odd.setCellValueFactory(new PropertyValueFactory<>("team1Odd"));
        tableView.getColumns().add(team1Odd);
        team1Odd.setResizable(false);
        team1Odd.setSortable(false);
        team1Odd.setReorderable(false);



        TableColumn<Game, Void> bet1Column = new TableColumn<>("Bet");
        bet1Column.setCellFactory(column -> new TableCell<Game, Void>() {
            private final Button betButton = new Button("Bet");

            {
                // Set up the button action here, but don't retrieve the game yet.
                betButton.setOnAction(event -> {
                    // Retrieve the Game object at the time of the button click.
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        try {
                            new BetView().betView(stage, game, game.getTeam1());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Now that the cell is associated with the table, get the Game object safely.
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        // Update button state (for example, disable if user already bet)
                        betButton.setDisable(user.checkBet(game));
                    }
                    setGraphic(betButton);
                }
            }
        });
        tableView.getColumns().add(bet1Column);
        bet1Column.setResizable(false);
        bet1Column.setReorderable(false);
        bet1Column.setSortable(false);





        TableColumn<Game, String> team2Odd = new TableColumn<>("Team 2 Odds");
        team2Odd.setCellValueFactory(new PropertyValueFactory<>("team2Odd"));
        tableView.getColumns().add(team2Odd);
        team2Odd.setResizable(false);
        team2Odd.setSortable(false);
        team2Odd.setReorderable(false);


        TableColumn<Game, Void> bet2Column = new TableColumn<>("Bet");
        bet2Column.setCellFactory(column -> new TableCell<Game, Void>() {
            private final Button betButton = new Button("Bet");

            {
                // Set up the button action here, but don't retrieve the game yet.
                betButton.setOnAction(event -> {
                    // Retrieve the Game object at the time of the button click.
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        try {
                            new BetView().betView(stage, game, game.getTeam2());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Now that the cell is associated with the table, get the Game object safely.
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        // Update button state (for example, disable if user already bet)
                        betButton.setDisable(user.checkBet(game));
                    }
                    setGraphic(betButton);
                }
            }
        });
        tableView.getColumns().add(bet2Column);

        bet2Column.setResizable(false);
        bet2Column.setReorderable(false);
        bet2Column.setSortable(false);



        for (Game game : response) {
            tableView.getItems().add(game);
        }

        try {

            userInfo.setBackground(Background.fill(Color.rgb(126, 24, 145)));
            Label userName = new Label(user.getName());
            userName.setFont(new Font(20));
            userName.setTextFill(Color.WHITE);

            Label money = new Label("$" + user.getMoney());
            money.setFont(new Font(20));
            money.setTextFill(Color.WHITE);




            if (user.getBets().isEmpty()) {
                Label emptyLabel = new Label("Your bets will appear here");
                emptyLabel.setFont(Font.font("System", FontPosture.ITALIC, 12));
                betList.getChildren().add(emptyLabel);
            } else {
                for (Bet bet : user.getBets()) {
                    VBox betBox = new VBox(10);
                    betBox.setPrefHeight(200);
                    betBox.setPrefWidth(200);
                    betBox.setStyle("-fx-background-color: #F26B0F");
                    Label game = new Label(bet.getGame().getTeam1() + " vs " + bet.getGame().getTeam2());
                    Label date = new Label(bet.getGame().getStartDate().toString());
                    game.setFont(new Font(15));
                    game.setTextFill(Color.WHITE);
                    date.setFont(new Font(15));
                    date.setTextFill(Color.WHITE);
                    HBox amts = new HBox(10);
                    Label betAmt = new Label("Bet $" + bet.getBetAmt());
                    betAmt.setFont(new Font(10));
                    betAmt.setTextFill(Color.WHITE);
                    Label winAmt = new Label("Win $" + bet.getWinAmt());
                    winAmt.setFont(new Font(10));
                    winAmt.setTextFill(Color.WHITE);
                    Button betInfo = new Button("See More");
                    betInfo.setOnAction(event -> {
                        try {
                            new BetInfoView().betInfoView(stage, bet);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    amts.getChildren().addAll(betAmt, winAmt);
                    betBox.getChildren().addAll(game, date, betAmt, winAmt, betInfo);
                    betList.getChildren().add(betBox);
                }
            }

            userInfo.getChildren().addAll(userName, money);
        } catch (Exception e) {
            e.printStackTrace();
        }

        labelView.setPadding(new Insets(20));


        borderPane.setCenter(tableView);
        borderPane.setBottom(betList);
        borderPane.setTop(userInfo);

        Scene scene = new Scene(borderPane, 1200, 600);
        stage.setScene(scene);
        stage.setTitle("Marauder Bets");
        stage.show();


    }
} //end class Client

