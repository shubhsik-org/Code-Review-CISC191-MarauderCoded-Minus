package edu.sdccd.cisc191.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the <code>Game</code> class.
 * This test class validates the functionality of the methods in the
 * <code>Game</code> class, including constructors, getters, setters,
 * and overridden methods.
 *
 * @author Andy Ly
 */
class GameTest {

    private Game game1;
    private Game game2;

    /**
     * Sets up test data before each test case.
     */
    @BeforeEach
    void setUp() {
        Date startDate = new Date(2025, 3, 10); // April 10, 2025 (month is 0-based)
        Date endDate = new Date(2025, 3, 15);  // April 15, 2025
        game1 = new Game("Team A", "Team B", startDate, endDate);
        game2 = new Game("Team C", "Team D", startDate, endDate, 50.0, 60.0);
    }

    /**
     * Tests the constructor that initializes mock betting odds.
     */
    @Test
    void testConstructorWithMockOdds() {
        assertEquals("Team A", game1.getTeam1(), "Team 1 should be 'Team A'.");
        assertEquals("Team B", game1.getTeam2(), "Team 2 should be 'Team B'.");
        assertNotNull(game1.getStartDate(), "The start date should not be null.");
        assertNotNull(game1.getEndDate(), "The end date should not be null.");
        assertTrue(game1.getTeam1Odd() > 0, "Team 1 odds should be greater than 0.");
        assertTrue(game1.getTeam2Odd() > 0, "Team 2 odds should be greater than 0.");
    }

    /**
     * Tests the constructor that accepts predefined betting odds.
     */
    @Test
    void testConstructorWithPredefinedOdds() {
        assertEquals("Team C", game2.getTeam1(), "Team 1 should be 'Team C'.");
        assertEquals("Team D", game2.getTeam2(), "Team 2 should be 'Team D'.");
        assertEquals(50.0, game2.getTeam1Odd(), 0.0001, "Team 1 odds should be 50.0.");
        assertEquals(60.0, game2.getTeam2Odd(), 0.0001, "Team 2 odds should be 60.0.");
    }

    /**
     * Tests the <code>getDateClean</code> method to ensure it returns the correct date string.
     */
    @Test
    void testGetDateClean() {
        String expectedDateClean = "3/10/2025 - 3/15/2025";
        assertEquals(expectedDateClean, game1.getDateClean(), "The cleaned date string is incorrect.");
    }

    /**
     * Tests the <code>toString</code> method for correct output format.
     */
    @Test
    void testToString() {
        String expectedString = "Team A vs. Team B on 3/10/2025";
        assertEquals(expectedString, game1.toString(), "The string representation of the game is incorrect.");
    }

    /**
     * Tests the <code>equals</code> method to ensure correct equality comparison.
     */
    @Test
    void testEquals() {
        Date startDate = new Date(2025, 3, 10);
        Date endDate = new Date(2025, 3, 15);
        Game duplicateGame = new Game("Team A", "Team B", startDate, endDate, game1.getTeam1Odd(), game1.getTeam2Odd());

        assertTrue(game1.equals(duplicateGame), "The two games should be equal.");
        assertFalse(game1.equals(game2), "The two games should not be equal.");
    }

    /**
     * Tests the <code>setTeam1</code> and <code>getTeam1</code> methods.
     */
    @Test
    void testSetAndGetTeam1() {
        game1.setTeam1("Team X");
        assertEquals("Team X", game1.getTeam1(), "Team 1 should be updated to 'Team X'.");
    }

    /**
     * Tests the <code>setTeam2</code> and <code>getTeam2</code> methods.
     */
    @Test
    void testSetAndGetTeam2() {
        game1.setTeam2("Team Y");
        assertEquals("Team Y", game1.getTeam2(), "Team 2 should be updated to 'Team Y'.");
    }

    /**
     * Tests the <code>setStartDate</code> and <code>getStartDate</code> methods.
     */
    @Test
    void testSetAndGetStartDate() {
        Date newStartDate = new Date(2025, 4, 1); // May 1, 2025
        game1.setStartDate(newStartDate);
        assertEquals(newStartDate, game1.getStartDate(), "The start date should be updated.");
    }

    /**
     * Tests the <code>setEndDate</code> and <code>getEndDate</code> methods.
     */
    @Test
    void testSetAndGetEndDate() {
        Date newEndDate = new Date(2025, 4, 10); // May 10, 2025
        game1.setEndDate(newEndDate);
        assertEquals(newEndDate, game1.getEndDate(), "The end date should be updated.");
    }
}
