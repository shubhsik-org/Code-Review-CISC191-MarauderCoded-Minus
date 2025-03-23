package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A singleton class that manages a database of games.
 * It provides functionalities to load, save, and access the database.
 *
 * <p>The data is stored in JSON format, and the database is thread-safe
 * to ensure proper operation in concurrent environments.</p>
 *
 * @author Andy Ly
 */
public class GameDatabase {

    // Singleton instance
    private static GameDatabase instance;

    private static final List<Game> gameDatabase = Collections.synchronizedList(new ArrayList<>());

    // File path for storing game data
    private static final String FILE_PATH = "Resources/games.json";

    /**
     * Private constructor to prevent instantiation outside the class.
     * Initializes the database by either loading data from the file
     * or creating a default dataset.
     */
    private GameDatabase() {
        loadOrInitializeDatabase();
    }

    /**
     * Retrieves the singleton instance of the <code>GameDatabase</code> class.
     *
     * @return The singleton <code>GameDatabase</code> instance.
     */
    public static synchronized GameDatabase getInstance() {
        if (instance == null) {
            instance = new GameDatabase();
        }
        return instance;
    }

    /**
     * Loads the game database from a JSON file if it exists, or initializes
     * it with default data if the file is not found.
     */
    void loadOrInitializeDatabase() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Game.class);
                List<Game> games = objectMapper.readValue(file, listType);
                gameDatabase.clear();
                gameDatabase.addAll(games);
                System.out.println("GameDatabase loaded from file.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load GameDatabase from file. Initializing with default data.");
                initializeDefaultGames();
                saveToFile();
            }
        } else {
            System.out.println("GameDatabase file not found. Initializing with default data.");
            initializeDefaultGames();
            saveToFile();
        }
    }

    /**
     * Initializes the game database with default data.
     */
    private void initializeDefaultGames() {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            gameDatabase.add(new Game(
                    String.format("Team %d", count),
                    String.format("Team %d", count + 1), new Date(),
                    new Date(count, count % 12, count % 12)));
            count += 2;
        }
    }

    /**
     * Saves the current state of the game database to a JSON file.
     */
    void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, gameDatabase);
            System.out.println("GameDatabase saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an unmodifiable view of the game database.
     *
     * @return An unmodifiable <code>List</code> of games.
     */
    public synchronized List<Game> getGameDatabase() {
        return Collections.unmodifiableList(gameDatabase);
    }

    /**
     * Gets the size of the game database.
     *
     * @return The size of the database as a <code>String</code>.
     */
    public synchronized String getSize() {
        return String.valueOf(gameDatabase.size());
    }
}
