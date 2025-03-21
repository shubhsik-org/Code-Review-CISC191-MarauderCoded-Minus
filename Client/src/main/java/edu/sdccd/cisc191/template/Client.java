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

    // --- Socket and Request Methods ---
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

    // -- Request Wrappers ---

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

    // --- UI Component Builders ---
    // Build the table view for the games
    private TableView<Game> createGameTableView(Game[] games, Stage stage) {
        TableView<Game> tableView = new TableView<>();

        // Column for Team 1
        TableColumn<Game, String> team1Col = new TableColumn<>("Team 1");
        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));
        tableView.getColumns().add(team1Col);
        team1Col.setResizable(false);
        team1Col.setSortable(false);
        team1Col.setReorderable(false);

        // Column for Team 2
        TableColumn<Game, String> team2Col = new TableColumn<>("Team 2");
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));
        tableView.getColumns().add(team2Col);
        team2Col.setResizable(false);
        team2Col.setSortable(false);
        team2Col.setReorderable(false);

        // Column for Date
        TableColumn<Game, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateClean"));
        dateCol.setPrefWidth(150);
        tableView.getColumns().add(dateCol);
        dateCol.setResizable(false);
        dateCol.setSortable(false);
        dateCol.setReorderable(false);

        // Column for Team 1 Odds
        TableColumn<Game, String> team1OddCol = new TableColumn<>("Team 1 Odds");
        team1OddCol.setCellValueFactory(new PropertyValueFactory<>("team1Odd"));
        tableView.getColumns().add(team1OddCol);
        team1OddCol.setResizable(false);
        team1OddCol.setSortable(false);
        team1OddCol.setReorderable(false);

        // Button column for betting on Team 1
        TableColumn<Game, Void> bet1Column = new TableColumn<>("Bet");
        bet1Column.setCellFactory(column -> new TableCell<Game, Void>() {
            private final Button betButton = new Button("Bet");
            {
                betButton.setOnAction(event -> {
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        try {
                            new BetView().betView(stage, game, game.getTeam1());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    betButton.getStyleClass().add("primary-button");
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        betButton.setDisable(user.checkBet(game));
                    }
                    setGraphic(betButton);
                }
            }
        });
        tableView.getColumns().add(bet1Column);
        bet1Column.setResizable(false);
        bet1Column.setSortable(false);
        bet1Column.setReorderable(false);

        // Column for Team 2 Odds
        TableColumn<Game, String> team2OddCol = new TableColumn<>("Team 2 Odds");
        team2OddCol.setCellValueFactory(new PropertyValueFactory<>("team2Odd"));
        tableView.getColumns().add(team2OddCol);
        team2OddCol.setResizable(false);
        team2OddCol.setSortable(false);
        team2OddCol.setReorderable(false);

        // Button column for betting on Team 2
        TableColumn<Game, Void> bet2Column = new TableColumn<>("Bet");
        bet2Column.setCellFactory(column -> new TableCell<Game, Void>() {
            private final Button betButton = new Button("Bet");
            {
                betButton.setOnAction(event -> {
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
                betButton.getStyleClass().add("primary-button");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        Game game = getTableView().getItems().get(index);
                        betButton.setDisable(user.checkBet(game));
                    }
                    setGraphic(betButton);
                }
            }
        });
        tableView.getColumns().add(bet2Column);
        bet2Column.setResizable(false);
        bet2Column.setSortable(false);
        bet2Column.setReorderable(false);

        tableView.getStyleClass().add("custom-table");

        // Add game items to the table view
        tableView.getItems().addAll(games);

        return tableView;
    }

    private HBox createUserInfoBox() {
        HBox userInfo = new HBox(10);
        userInfo.setBackground(Background.fill(Color.rgb(126, 24, 145)));

        Label userName = new Label(user.getName());
        userName.setFont(new Font(20));
        userName.setTextFill(Color.WHITE);

        Label money = new Label("$" + user.getMoney());
        money.setFont(new Font(20));
        money.setTextFill(Color.WHITE);

        Label moneyLine = new Label("$" + user.getMoneyLine());
        moneyLine.setFont(new Font(20));
        moneyLine.setTextFill(Color.LIGHTGRAY);

        Label moneyBet = new Label("$" + user.getMoneyBet());
        moneyBet.setFont(new Font(20));
        moneyBet.setTextFill(Color.LIGHTGRAY);

        userInfo.getChildren().addAll(userName, money, moneyLine, moneyBet);
        return userInfo;
    }

    private HBox createBetListBox(Stage stage) {
        HBox betList = new HBox(10);
        betList.setPrefHeight(200);

        if (user.getBets().isEmpty()) {
            Label emptyLabel = new Label("Your bets will appear here");
            emptyLabel.setFont(Font.font("System", FontPosture.ITALIC, 12));
            betList.getChildren().add(emptyLabel);
        } else {
            for (Bet bet : user.getBets()) {
                VBox betBox = new VBox(10);
                betBox.setPrefHeight(200);
                betBox.setPrefWidth(200);
                betBox.getStyleClass().add("bet-box");

                Label gameLabel = new Label(bet.getGame().getTeam1() + " vs " + bet.getGame().getTeam2());
                Label dateLabel = new Label(bet.getGame().getDateClean());
                Label teamLabel = new Label(bet.getBetTeam());

                // Apply a common style class to all bet-related labels
                gameLabel.getStyleClass().add("bet-box-label");
                dateLabel.getStyleClass().add("bet-box-label");
                teamLabel.getStyleClass().add("bet-box-label");

                Label betAmt = new Label("Bet $" + bet.getBetAmt());
                betAmt.setFont(new Font(10));
                betAmt.setTextFill(Color.WHITE);
                Label winAmt = new Label("Win $" + bet.getWinAmt());
                winAmt.setFont(new Font(10));
                winAmt.setTextFill(Color.WHITE);

                Button betInfo = new Button("See More");
                betInfo.getStyleClass().add("bet-info-button");
                betInfo.setOnAction(event -> {
                    try {
                        new BetInfoView().betInfoView(stage, bet);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                betBox.getChildren().addAll(gameLabel, dateLabel, teamLabel, betAmt, winAmt, betInfo);
                betList.getChildren().add(betBox);
            }
        }
        return betList;
    }

    private Game[] getGames() throws IOException {
        int size = getSizeRequest(1);
        Game[] games = new Game[size];
        for (int i = 0; i < size; i++) {
            games[i] = this.gameGetRequest(i);
        }
        return games;
    }



    public static void main(String[] args) {
        launch();// Run this Application.
    }


    @Override
    public void start(Stage stage) throws Exception {

        // Build the main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        // Retrieve game data (and user data if needed)
        Game[] games = getGames();
        // Note: The sample user array in the original code is replaced by the static "user" field.

        // Create UI components
        TableView<Game> gameTable = createGameTableView(games, stage);
        HBox userInfoBox = createUserInfoBox();
        HBox betListBox = createBetListBox(stage);

        // Assemble components into the BorderPane
        borderPane.setCenter(gameTable);
        borderPane.setTop(userInfoBox);
        borderPane.setBottom(betListBox);

        // Create and set the scene
        Scene scene = new Scene(borderPane, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Marauder Bets");
        stage.show();
    }
} //end class Client

