import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Client thread " + socket.getLocalPort());

            System.out.println("Connected to the server.");
            System.out.println("How many threads do you want to run the BFS?");

            String threadCount = scanner.nextLine();
            writer.println(threadCount);

            while (true) {
                System.out.println("Enter the graph as list of edges, followed by the starting vertex:");

                String message = scanner.nextLine();
                while (message.contains(" ")) {
                    writer.println(message);
                    message = scanner.nextLine();
                }
                writer.println(message);

                String reply;
                while ((reply = reader.readLine()) != null && !reply.contains("Time")) {
                    System.out.println(reply);
                }
                if (reply != null && reply.contains("Time"))
                    System.out.println(reply);
            }

        } catch (IOException e) {
            throw new RuntimeException("There was a problem with the network communication", e);
        }
    }
}
