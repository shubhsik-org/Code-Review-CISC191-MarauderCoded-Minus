package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

public class Game {
//    private Integer id;
//    private String firstName;
//    private String lastName;


    private String team1;
    private String team2;
    private Date date;


    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(Game customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    public static Game fromJSON(String input) throws Exception{
        return objectMapper.readValue(input, Game.class);
    }
    protected Game() {}

    public Game(String t1, String t2, Date Date) {
        this.team1 = t1;
        this.team2 = t2;
        this.date = Date;
    }

    @Override
    public String toString() {
        return team1 + " vs. " + team2 + " on " + date;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public Date getDate() {
        return date;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}