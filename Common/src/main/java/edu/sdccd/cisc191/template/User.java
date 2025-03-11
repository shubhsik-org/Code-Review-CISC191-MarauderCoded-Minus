package edu.sdccd.cisc191.template;

import java.util.ArrayList;

public class User {
    private String name;
    private int money;
    private ArrayList<Bet> bets = new ArrayList<>();

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

    public void addBet (Bet b) {
        bets.add(b);
    }

    public void setMoney(int amt) {
        this.money += amt;
    }

    public void setName(String name) {
        this.name = name;
    }



}
