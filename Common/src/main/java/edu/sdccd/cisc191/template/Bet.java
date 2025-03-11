package edu.sdccd.cisc191.template;

public class Bet {
    private Game game;
    private int betAmt;
    private int winAmt;

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

    public int getBetAmt() {
        return betAmt;
    }

    public void setBetAmt(int betAmt) {
        this.betAmt = betAmt;
    }


    public Bet(Game g, int amt) {
        this.game = g;
        this.betAmt = amt;
        this.winAmt = (int) (amt * 1.5);
    }

    public String toString() {
        return "Bet on " + game.toString() + " for " + betAmt;
    }

}
