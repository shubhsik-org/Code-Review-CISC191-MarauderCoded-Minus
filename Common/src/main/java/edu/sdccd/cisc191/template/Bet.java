package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

/**
 * Represents a bet placed on a game. Contains information about the game,
 * the team being bet on, the bet amount, potential winnings, and the
 * odds of winning. Additionally, it tracks odds over time.
 *
 * <p>The class supports JSON serialization and deserialization for integration
 * with external systems and persistent storage.</p>
 *
 * @author Brian Tran, Andy Ly, Julian Garcia
 * @see Game
 * @see User
 */
public class Bet {

    private Game game;
    private String team;
    private int betAmt;
    private int winAmt;
    private int winOdds;

    private final int numHours = 10; // Number of hours to track odds
    private final double[][] winOddsOvertime = new double[numHours][2]; // Array to track odds over time

    private boolean wasFulfilled;
    private final long currentEpochSeconds = System.currentTimeMillis() / 1000; // Current time in seconds

    @JsonIgnore
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Serializes a <code>Bet</code> object into a JSON string.
     *
     * @param bet The <code>Bet</code> object to serialize.
     * @return A JSON string representation of the <code>Bet</code>.
     * @throws Exception If serialization fails.
     */
    public static String toJSON(Bet bet) throws Exception {
        return objectMapper.writeValueAsString(bet);
    }

    /**
     * Deserializes a JSON string into a <code>Bet</code> object.
     *
     * @param input The JSON string to deserialize.
     * @return A <code>Bet</code> object created from the JSON string.
     * @throws Exception If deserialization fails.
     */
    public static Bet fromJSON(String input) throws Exception {
        return objectMapper.readValue(input, Bet.class);
    }

    /**
     * Default constructor for <code>Bet</code>.
     * Required for JSON serialization/deserialization.
     */
    protected Bet() {
        // Default constructor for deserialization purposes
    }

    private final Random random = new Random();

    /**
     * Constructs a new <code>Bet</code> with specified game, bet amount, and team.
     * Initializes potential winnings, odds of winning, and odds tracking over time.
     *
     * @param g The game associated with the bet.
     * @param amt The amount of money being bet.
     * @param team The team being bet on.
     */
    public Bet(Game g, int amt, String team) {
        this.game = g;
        this.team = team;
        this.betAmt = amt;
        this.winAmt = (int) (amt * 1.5); // Example logic for calculating winnings
        this.winOdds = (int) Math.round(1 + Math.random() * 99); // Randomized winning odds

        // Populate winOddsOvertime with odds and timestamps
        for (int j = 0; j < numHours; j++) {
            long timeStamp = currentEpochSeconds - (j * 3600L); // Decrement by hours
            double odd = calculateOddsForGameAtTime(timeStamp);
            winOddsOvertime[j][0] = odd;
            winOddsOvertime[j][1] = timeStamp;
        }
    }

    /**
     * Calculates the odds for a game at a specific timestamp.
     *
     * @param timeStamp The timestamp for which to calculate the odds.
     * @return A random value representing the odds at the specified time.
     */
    private double calculateOddsForGameAtTime(long timeStamp) {
        return 1 + random.nextInt(100); // Generate a random value between 1 and 100
    }

    /**
     * Gets the potential winnings for the bet.
     *
     * @return The winning amount.
     */
    public int getWinAmt() {
        return winAmt;
    }

    /**
     * Sets the potential winnings for the bet.
     *
     * @param winAmt The winning amount to set.
     */
    public void setWinAmt(int winAmt) {
        this.winAmt = winAmt;
    }

    /**
     * Gets the game associated with the bet.
     *
     * @return The associated game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Sets the game associated with the bet.
     *
     * @param game The game to set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the odds of winning the bet.
     *
     * @return The odds of winning as a percentage.
     */
    public double getWinOdds() {
        return winOdds;
    }

    /**
     * Gets the odds tracked over a 10-hour period.
     *
     * @return A 2D array representing odds and timestamps.
     */
    public double[][] getWinOddsOvertime() {
        return winOddsOvertime;
    }

    /**
     * Updates the user's money based on the outcome of the bet.
     *
     * @param user The user associated with the bet.
     * @return The updated user object.
     */
    public User updateUser(User user) {
        if (wasFulfilled) {
            user.setMoney(user.getMoney() + winAmt);
        } else {
            user.setMoney(user.getMoney() - winAmt);
        }
        return user;
    }

    /**
     * Updates the fulfillment status of the bet based on the odds of winning.
     */
    public void updateFulfillment() {
        int randomNumber = random.nextInt(100) + 1; // Generate a number from 1 to 100
        wasFulfilled = randomNumber <= winOdds;
    }

    /**
     * Gets the fulfillment status of the bet based on the odds of winning.
     */
    public boolean getFulfillment() {
        return this.wasFulfilled;
    }

    /**
     * Gets the team being bet on.
     *
     * @return The team being bet on.
     */
    public String getBetTeam() {
        return team;
    }

    /**
     * Gets the bet amount.
     *
     * @return The bet amount.
     */
    public int getBetAmt() {
        return betAmt;
    }

    /**
     * Sets the bet amount.
     *
     * @param betAmt The amount of money to set for the bet.
     */
    public void setBetAmt(int betAmt) {
        this.betAmt = betAmt;
    }

    /**
     * Converts the <code>Bet</code> object into a string representation.
     *
     * @return A string describing the bet.
     */
    @Override
    public String toString() {
        return "Bet on " + game + " for " + betAmt;
    }
}
