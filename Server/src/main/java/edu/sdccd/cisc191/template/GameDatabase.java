package edu.sdccd.cisc191.template;

import java.util.*;

// Singleton to store all users. CRUD on user database will be implemented here.
public class GameDatabase {

        private static final List<Game> gameDatabase = Collections.synchronizedList(new ArrayList<>());

        // Populate database
        static {
            int count = 0;
            for (int i = 0; i < 6; i++) {
                gameDatabase.add(new Game(String.format("Team %d", count), String.format("Team %d", count + 1), new Date(2025 + count, count % 12, count % 12)));
                count += 2;
            }
        }

        public static synchronized List<Game> getGameDatabase() {
            return gameDatabase;
        }

        // Returns string because we should only be calling this in ClientHandler, which deals only with JSON strings
        public static synchronized String getSize() {

            return String.valueOf(gameDatabase.size());

        }
}
