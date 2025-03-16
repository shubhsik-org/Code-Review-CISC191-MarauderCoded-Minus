package edu.sdccd.cisc191.template;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
    private String name;
    private int money;
    // TODO: Check by using this list instead of looping through all bets on the table.
    // We are doing wayy too many checks right now.
    private ArrayList<Bet> bets = new ArrayList<>();

    //BEGIN MAKING CLASS SERIALIZABLE
    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(User customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    public static User fromJSON(String input) throws Exception {
        //System.out.println(input);
        return objectMapper.readValue(input, User.class);
    }

    protected User() {}
    //END MAKING CLASS SERIALIZABLE

    public User(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public boolean checkBet(Game game) {
        for (Bet bet : bets) {
            boolean result = bet.getGame().equals(game);
            System.out.println("Checking bet: " + bet.getGame() + " with game: " + game + " Result: " + result);
            if (result) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Bet> getBets() {
        return bets;
    }

    public void addBet(Bet b) {
        bets.add(b);
    }

    public void removeBet(Bet b) {
        bets.remove(b);
    }

    public void setMoney(int amt) {
        this.money += amt;
    }

    public void setName(String name) {
        this.name = name;
    }



}
