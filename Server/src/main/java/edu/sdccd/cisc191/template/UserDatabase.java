package edu.sdccd.cisc191.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Singleton to store all users. CRUD on user database will be implemented here.
public class UserDatabase {

        private static final List<User> userDatabase = Collections.synchronizedList(new ArrayList<>());

        // Populate database
        static {
            for (int i = 0; i < 5; i++) {
                userDatabase.add(new User(UUID.randomUUID().toString().replace("-", "").substring(0, 10), (int) (Math.random() * 1000)));
            }
        }

        public static synchronized List<User> getUserDatabase() {
            return userDatabase;
        }
}
