package edu.sdccd.cisc191.template;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game customerResponse;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        customerResponse = new Game("User", "Test", new Date(), new Date(2025, 3, 24));
    }

    @org.junit.jupiter.api.Test
    void getCustomer() {
        assertEquals(customerResponse.toString(), "Customer[id=1, firstName='Test', lastName='User']");
    }

    @org.junit.jupiter.api.Test
    void setCustomer() {
        customerResponse.setTeam1("User");
        customerResponse.setTeam2("Test");
        assertEquals(customerResponse.toString(), "Customer[id=1, firstName='User', lastName='Test']");
    }
}