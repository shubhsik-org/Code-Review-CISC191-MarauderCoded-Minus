package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
            Integer amount = Integer.parseInt(b.getText());
            if (Client.user.getMoneyBet() >= amount) {
                Bet placedBet = new Bet(game, amount, team);
                Client.user.addBet(placedBet);
                try {
                    new Client().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                //Creating a dialog
                Dialog<String> dialog = new Dialog<String>();
                //Setting the title
                dialog.setTitle("Marauder Bets");
                ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
                //Setting the content of the dialog
                dialog.setContentText("This is more money that you have available to bet! $" + Client.user.getMoneyBet());
                //Adding buttons to the dialog pane
                dialog.getDialogPane().getButtonTypes().add(type);
                dialog.showAndWait();
            }
        });
        stage.setScene(new Scene(betView, 200, 300));
        stage.show();


    }
}
