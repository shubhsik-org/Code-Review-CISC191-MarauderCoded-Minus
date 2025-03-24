package edu.sdccd.cisc191.template;

public class BetTest {

   import java.util.Random;

    public class BettingOddsAndTime {

        public static void main(String[] args) {
            int rows = 5;
            int columns = 2;

            int[][] data = new int[rows][columns];

            Random rand = new Random();

            for (int i = 0; i < rows; i++) {
                data[i][0] = rand.nextInt(100) + 1;

                data[i][1] = rand.nextInt(24) + 1;
            }

            System.out.println("Betting Odds and Time:");
            for (int i = 0; i < rows; i++) {
                System.out.println("Betting Odds: " + data[i][0] + ", Time: " + data[i][1] + " hours");
            }
        }
    }

}
    // TODO: Test historical betting odds 2D array
}
