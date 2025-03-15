package edu.sdccd.cisc191.template;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {
    private String name;
    private int money;
    private ArrayList<Bet> bets = new ArrayList<>();

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJSON(User customer) throws Exception {
        return objectMapper.writeValueAsString(customer);
    }

    public static User fromJSON(String input) throws Exception {
        System.out.println(input);
        return objectMapper.readValue(input, User.class);
    }

    protected User() {}

    public User(String name, int money) {
        this.name = name;
        this.money = money;
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

    public void setMoney(int amt) {
        this.money += amt;
    }

    public void setName(String name) {
        this.name = name;
    }



}
