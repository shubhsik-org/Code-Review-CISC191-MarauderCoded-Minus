package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class Bet {
    private Game game;
    private String team;
    private int betAmt;
    private int winAmt;
    private int winOdds;

    // Track odds over the last 10 hours for one game only.
    int numHours = 10;
    // Each row: [odd, timestamp in seconds since epoch]
    double[][] winOddsOvertime = new double[numHours][2];

    private boolean wasFulfilled;
    long currentEpochSeconds = System.currentTimeMillis() / 1000; // current time in seconds

    // BEGIN MAKING CLASS SERIALIZABLE
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(Bet bet) throws Exception {
        return objectMapper.writeValueAsString(bet);
    }

    public static Bet fromJSON(String input) throws Exception {
        return objectMapper.readValue(input, Bet.class);
    }

    protected Bet() {}
    // END MAKING CLASS SERIALIZABLE

    Random random = new Random();

    // Betting entry and return calculations
    public Bet(Game g, int amt, String team) {
        this.game = g;
        this.team = team;
        this.betAmt = amt;
        this.winAmt = (int) (amt * 1.5);
        this.winOdds = (int) Math.round(1 + Math.random() * 99);

        // Populate winOddsOvertime with pairs: [odd, timestamp]
        for (int j = 0; j < numHours; j++) {
            long timeStamp = currentEpochSeconds - (j * 3600L); // decrement j hours
            double odd = calculateOddsForGameAtTime(timeStamp);
            winOddsOvertime[j][0] = odd;
            winOddsOvertime[j][1] = timeStamp;
        }
    }

    // Updated method: generates a random odd between 1 and 100 (dummy logic).
    private double calculateOddsForGameAtTime(long timeStamp) {
        return 1 + random.nextInt(100); // random value between 1 and 100
    }

    public int getWinAmt() {
        return winAmt;
    }

    public void setWinAmt(int winAmt) {
        this.winAmt = winAmt;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public double getWinOdds() {
        return winOdds;
    }

    public double[][] getWinOddsOvertime() {
        return winOddsOvertime;
    }

    public User updateUser(User user) {
        if (wasFulfilled) {
            user.setMoney(user.getMoney() + winAmt);
        } else {
            user.setMoney(user.getMoney() - winAmt);
        }
        return user;
    }

    public void updateFulfillment() {
        int randomNumber = random.nextInt(100) + 1; // generates a number from 1 to 100
        wasFulfilled = randomNumber <= winOdds;
    }

    public String getBetTeam() {
        return team;
    }

    public int getBetAmt() {
        return betAmt;
    }

    public void setBetAmt(int betAmt) {
        this.betAmt = betAmt;
    }

    @Override
    public String toString() {
        return "Bet on " + game + " for " + betAmt;
    }
}
