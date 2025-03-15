package edu.sdccd.cisc191.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserDatabase {

    // Singleton instance
    private static UserDatabase instance;

    // Synchronized list to store users
    private static final List<User> userDatabase = Collections.synchronizedList(new ArrayList<>());

    // Path to the JSON file
    private static final String FILE_PATH = "Server/Users.json";

    // Private constructor to prevent instantiation
    private UserDatabase() {
        loadOrInitializeDatabase();
    }

    // Public method to get the Singleton instance
    public static synchronized UserDatabase getInstance() {
        if (instance == null) {
            instance = new UserDatabase();
        }
        return instance;
    }

    // Load users from JSON or initialize with default data
    private void loadOrInitializeDatabase() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            // Load from JSON file
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
                List<User> users = objectMapper.readValue(file, listType);
                userDatabase.clear();
                userDatabase.addAll(users);
                System.out.println("UserDatabase loaded from file.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load UserDatabase from file. Initializing with default data.");
                initializeDefaultUsers();
                saveToFile();
            }
        } else {
            // Initialize with default data and save to JSON
            System.out.println("UserDatabase file not found. Initializing with default data.");
            initializeDefaultUsers();
            saveToFile();
        }
    }

    // Initialize with default data
    private void initializeDefaultUsers() {
        for (int i = 0; i < 5; i++) {
            userDatabase.add(new User(UUID.randomUUID().toString().replace("-", "").substring(0, 10), (int) (Math.random() * 1000)));
        }
    }

    // Save the current database to JSON
    public void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(writer, userDatabase);
            System.out.println("UserDatabase saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Public method to access users
    public synchronized List<User> getUserDatabase() {
        return Collections.unmodifiableList(userDatabase); // Return an unmodifiable view for safety
    }

    // Get the size of the database
    public synchronized String getSize() {
        return String.valueOf(userDatabase.size());
    }
}
