package edu.sdccd.cisc191.template;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClient {
    public static void main(String[] args) throws Exception {

        // Log intial user state
        Client client = new Client();
        client.startConnection("localhost", 4444);
        User initialUser = client.userGetRequest(2);
        System.out.println("Initial User State: " + initialUser);
        client.stopConnection();

        // Create a thread pool for concurrent client requests
        ExecutorService executor = Executors.newFixedThreadPool(2000);
        // Modify same user object using testRuns amount of times.
        int testRuns = 3000;
        for (int i = 0; i < testRuns; i++) {
            executor.submit(() -> {
                try {

                    client.startConnection("localhost", 4444);

                    // Modify User ID 2 with a unique modification
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("Name", "User" + Thread.currentThread().getId());
                    attributes.put("Money", 1);

                    User modifiedUser = client.userModifyRequest(2, attributes);
                    System.out.println("Modified User: " + modifiedUser);

                    client.stopConnection();
                    // Stagger the requests
                    Thread.sleep((long) (Math.random()*100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Shut down the executor after tasks complete
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all tasks to finish
        }

        // Retrieve the final state of User ID 2
        client.startConnection("localhost", 4444);
        User finalUser = client.userGetRequest(2);
        System.out.println(String.format("Final User State: Name[%s], Money[%d}", finalUser.getName(), finalUser.getMoney()));
        assert finalUser.getBets().size() == testRuns; // Ensure all bets were added
        assert finalUser.getMoney() == initialUser.getMoney() + testRuns;
        System.out.println("Assertions passed!");
        client.stopConnection();
    }
}
