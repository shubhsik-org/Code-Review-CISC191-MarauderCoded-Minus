package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Bet {
    private Game game;
    private int betAmt;
    private int winAmt;
    private int winOdds;
    private int[] winOddsOvertime;

    //BEGIN MAKING CLASS SERIALIZABLE
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(Bet customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    public static Bet fromJSON(String input) throws Exception {
        // System.out.println(input);
        return objectMapper.readValue(input, Bet.class);
    }

    protected Bet() {}
    //END MAKING CLASS SERIALIZABLE

    //Betting entry and return calculations
    public Bet(Game g, int amt) {
        this.game = g;
        this.betAmt = amt;
        this.winAmt = (int) (amt * 1.5);
        this.winOdds = (int) Math.round(1 + Math.random() * 99);
        this.winOddsOvertime = new int[20];
        for (int i = 0; i < 20; i++) {
            this.winOddsOvertime[i] = (int) Math.round(1 + Math.random() * 99);
        }
    /*
        this.winOdds = odds;
       if (winOdds >= 0) {
           this.winAmt = (int) (amt + (odds / 100) * amt);
       }
       else {
           this.winAmt = (int) (amt + (100 / Math.abs(odds)) * amt);
       }
    */
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

    public int[] getWinOddsOvertime() {
        return winOddsOvertime;
    }


    public int getBetAmt() {
        return betAmt;
    }

    public void setBetAmt(int betAmt) {
        this.betAmt = betAmt;
    }

    public String toString() {
        return "Bet on " + game + " for " + betAmt;
    }

}
