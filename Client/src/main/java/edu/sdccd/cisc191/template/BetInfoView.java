package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BetInfoView extends Application {
    Bet bet;

    public void betInfoView(Stage primaryStage, Bet bet) throws Exception {
        this.bet = bet;
        start(primaryStage);

    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setStyle("-fx-alignment: center");

        Label betInfo = new Label(bet.toString());

        HBox money = new HBox(5);
        Label betAmt = new Label("$" + bet.getBetAmt());
        Label winAmt = new Label("$" + bet.getWinAmt());
        money.getChildren().addAll(betAmt, winAmt);
        money.setStyle("-fx-alignment: center");

        // Set up the X and Y axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Index");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Random Value");

        // Create the BarChart with the axes
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Random Numbers Chart");

        // Create a data series and add data points for each array element
        LineChart.Series<String, Number> series = new LineChart.Series<>();
        series.setName("Random Data");
        for (int i = 0; i < bet.getWinOddsOvertime().length; i++) {
            // Use index (starting from 1) as the category label
            series.getData().add(new XYChart.Data<>(String.valueOf(i + 1), bet.getWinOddsOvertime()[i]));
        }

        // Add the data series to the chart
        lineChart.getData().add(series);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                new Client().start(stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        root.getChildren().add(betInfo);
        root.getChildren().add(money);
        root.getChildren().add(lineChart);
        root.getChildren().add(backButton);

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Bet Info");
        stage.setScene(scene);
        stage.show();



    }
}
