package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class BetView extends Application {
    Game game;
    String team;


    public void betView(Stage stage, Game game, String team) throws Exception {
        this.game = game;
        this.team = team;
        start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox betView = new VBox(10);
        Label bet = new Label("How much do you want to bet?");
        TextField b = new TextField();
        Button b1 = new Button("Place Bet");

        betView.getChildren().addAll(bet, b, b1);

        b1.setOnAction(evt -> {
            Bet placedBet = new Bet(game, Integer.parseInt(b.getText()), team);
            Client.user.addBet(placedBet);
            try {
                new Client().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        stage.setScene(new Scene(betView, 200, 300));
        stage.show();



    }
}
