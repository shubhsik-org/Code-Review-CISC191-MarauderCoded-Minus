package edu.sdccd.cisc191.template;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.*;

public class ClientTest {

    private ServerSocket serverSocket;
    private Thread serverThread;

    /**
     * Sets up a ServerSocket on port 4444 before each test.
     */
    @BeforeEach
    public void setUp() throws IOException {
        // Port 4444 is used by the Client code.
        serverSocket = new ServerSocket(4444);
    }

    /**
     * Closes the ServerSocket and interrupts the server thread after each test.
     */
    @AfterEach
    public void tearDown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }

    /**
     * Starts a dummy server that waits for a connection, reads the first line,
     * optionally asserts that the request contains the expected type,
     * and then sends back the given response.
     *
     * @param expectedRequestType A substring expected to be in the client's request.
     * @param response The response string to send back.
     */
    private void startDummyServer(String expectedRequestType, String response) {
        serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request = in.readLine();
                // Optionally, verify the request contains the expected request type.
                assertTrue(request.contains(expectedRequestType));
                out.println(response);

            } catch (IOException e) {
                // In testing, print the stack trace to help with debugging.
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @Test
    public void testGameGetRequest() throws IOException {
        // Dummy JSON response for a Game.
        String gameJson = "{\"team1\": \"Team A\", \"team2\": \"Team B\", \"dateClean\": \"2025-03-22\", \"team1Odd\": \"1.5\", \"team2Odd\": \"2.5\"}";
        startDummyServer("Game", gameJson);

        Client client = new Client();
        Game game = client.gameGetRequest(0);
        assertNotNull(game, "Expected non-null Game response");
        assertEquals("Team A", game.getTeam1(), "Team1 should be 'Team A'");
        assertEquals("Team B", game.getTeam2(), "Team2 should be 'Team B'");
        assertEquals("1.5", "" +  game.getTeam1Odd(), "Team1 odd should be '1.5'");
        assertEquals("2.5", "" + game.getTeam2Odd(), "Team2 odd should be '2.5'");
    }

    @Test
    public void testUserGetRequest() throws IOException {
        // Dummy JSON response for a User.
        String userJson = "{\"name\": \"Test User\", \"money\": 1000}";
        startDummyServer("User", userJson);

        Client client = new Client();
        User user = client.userGetRequest(1);
        assertNotNull(user, "Expected non-null User response");
        assertEquals("Test User", user.getName(), "User name should be 'Test User'");
        assertEquals(1000, user.getMoney(), "User money should be 1000");
    }

    @Test
    public void testGetSizeRequest() throws IOException {
        // The dummy server returns a size as a plain integer in string format.
        startDummyServer("GetSize", "5");

        Client client = new Client();
        int size = client.getSizeRequest(2);
        assertEquals(5, size, "Expected size to be 5");
    }

    @Test
    public void testUserModifyRequest() throws IOException {
        // Dummy JSON response for a modified User.
        String modifiedUserJson = "{\"name\": \"Modified User\", \"money\": 2000}";
        startDummyServer("ModifyUser", modifiedUserJson);

        Client client = new Client();
        Map<String, Object> modifications = new HashMap<>();
        modifications.put("Name", "Modified User");
        modifications.put("Money", 2000);
        User user = client.userModifyRequest(1, modifications);
        assertNotNull(user, "Expected non-null User response after modification");
        assertEquals("Modified User", user.getName(), "User name should be updated to 'Modified User'");
        assertEquals(2000, user.getMoney(), "User money should be updated to 2000");
    }
}
