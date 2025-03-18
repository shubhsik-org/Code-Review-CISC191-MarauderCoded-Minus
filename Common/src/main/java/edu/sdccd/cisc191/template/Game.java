package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Objects;

public class Game {

    private String team1;
    private String team2;
    private Date startDate;
    private Date endDate;
    private String dateClean;
    private double team1Odd;
    private double team2Odd;

    //BEGIN MAKING CLASS SERIALIZABLE
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(Game customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    public static Game fromJSON(String input) throws Exception{
        // System.out.println(input);
        return objectMapper.readValue(input, Game.class);
    }
    protected Game() {}
    //END MAKING CLASS SERIALIZABLE
    //Easy constructor to use to mock odds data
    public Game(String t1, String t2, Date startDate, Date endDate) {
        this.team1 = t1;
        this.team2 = t2;
        this.startDate = startDate;
        this.endDate = endDate;

        this.team1Odd = Math.round(Math.random() * 100);
        this.team2Odd = Math.round(Math.random() * 100);

        this.dateClean = this.getDateClean();
    }


    // Constructor to use if we are loading game from API
    public Game(String t1, String t2, Date startDate, Date endDate, double team1Odd, double team2Odd) {
        this.team1 = t1;
        this.team2 = t2;
        this.startDate = startDate;
        this.endDate = endDate;

        this.team1Odd = team1Odd;
        this.team2Odd = team2Odd;
        this.dateClean = this.getDateClean();

    }

    //Calculating odds with American betting odds using pool of money
    /*
    public Game(String t1, String t2, Date startDate, Date endDate, double team1Odd, double team2Odd, double pool) {
       this.team1Wager = 80;
       this.team2Wager = 20;
       this.betPool = team1Wager + team2Wager;

       this.team1PayoutRatio = betPool / team1Wager;
       this.team2PayoutRatio = betPool / team2Wager;

       this.team1ProfitFactor = team1PayoutRatio - 1;
       this.team2ProfitFactor = team2PayoutRatio - 1;

       if (team1ProfitFactor >= 1) {
            this.team1Odd = +(team1ProfitFactor * 100);
       }
       else {
            this.team1Odd = -(100/team1ProfitFactor);
       }
       if (this.team2ProfitFactor >= 1) {
            this.team2Odd = +(team2ProfitFactor * 100);
       }
       else {
            this.team2Odd = -(100/team2ProfitFactor);
       }
    }
    */

    @Override
    public String toString() {
        return team1 + " vs. " + team2 + " on " + startDate.getMonth() + "/" + startDate.getDate() + "/" + startDate.getYear();
    }

    // Overriding equals method so we can compare game objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Game game = (Game) obj;

        boolean team1Equals = Objects.equals(this.team1, game.getTeam1());
        boolean team2Equals = Objects.equals(this.team2, game.getTeam2());
        boolean startDateEquals = this.startDate.compareTo(game.getStartDate()) == 0; // compareTo method is proper for comparing dates
        boolean endDateEquals = this.endDate.compareTo(game.getEndDate()) == 0;
        boolean team1OddEquals = Math.abs(this.team1Odd - game.getTeam1Odd()) < 0.0001; // Account for potenional floating point error
        boolean team2OddEquals = Math.abs(this.team2Odd - game.getTeam2Odd()) < 0.0001;

        boolean result = team1Equals && team2Equals && startDateEquals && endDateEquals && team1OddEquals && team2OddEquals;

        /* System.out.println("Comparing games: " + this + " and " + game);
        System.out.println("team1Equals: " + team1Equals);
        System.out.println("team2Equals: " + team2Equals);
        System.out.println("startDateEquals: " + startDateEquals);
        System.out.println("endDateEquals: " + endDateEquals);
        System.out.println("team1OddEquals: " + team1OddEquals);
        System.out.println("team2OddEquals: " + team2OddEquals);
        System.out.println("Result: " + result); */

        return result;
    }
    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public double getTeam1Odd() {
        return team1Odd;
    }

    public double getTeam2Odd() {
        return team2Odd;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getDateClean() { return startDate.getMonth() + "/" + startDate.getDate() + "/" + startDate.getYear() + " - " + endDate.getMonth() + "/" + endDate.getDate() + "/" + endDate.getYear(); }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}