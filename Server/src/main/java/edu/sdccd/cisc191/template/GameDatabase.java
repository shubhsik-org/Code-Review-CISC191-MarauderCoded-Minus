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

// Singleton to store and load game database
public class GameDatabase {

    // Singleton instance
    private static GameDatabase instance;

    private static final List<Game> gameDatabase = Collections.synchronizedList(new ArrayList<>());

    // Create the file server side
    private static final String FILE_PATH = "Server/games.json";

    private GameDatabase() {
        loadOrInitializeDatabase();
    }

    // Singleton pattern
    public static synchronized GameDatabase getInstance() {
        if (instance == null) {
            instance = new GameDatabase();
        }
        return instance;
    }

    // Load from JSON or initialize with default data
    private void loadOrInitializeDatabase() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            // Load from JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Game.class);
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
            // Initialize with default data and save to JSON
            System.out.println("GameDatabase file not found. Initializing with default data.");
            initializeDefaultGames();
            saveToFile();
        }
    }

    // Initialize with default data
    private void initializeDefaultGames() {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            gameDatabase.add(new Game(
                    String.format("Team %d", count),
                    String.format("Team %d", count + 1), new Date(),
                    new Date(2025 + count, count % 12, count % 12)));
            count += 2;
        }
    }

    // Save the current database to JSON
    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, gameDatabase);
            System.out.println("GameDatabase saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Once we get the instance, use this to get the values
    public synchronized List<Game> getGameDatabase() {
        return Collections.unmodifiableList(gameDatabase); // Return an unmodifiable view for safety
    }

    // Get database size
    public synchronized String getSize() {
        return String.valueOf(gameDatabase.size());
    }
}
