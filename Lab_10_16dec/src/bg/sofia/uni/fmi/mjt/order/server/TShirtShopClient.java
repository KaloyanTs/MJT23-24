package bg.sofia.uni.fmi.mjt.order.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TShirtShopClient {

    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            Thread.currentThread().setName("Echo client thread " + socket.getLocalPort());

            System.out.println("Connected to the shop server.");
            System.out.println();

            while (true) {
                String message = scanner.nextLine(); // read a line from the console

                if ("disconnect".equals(message)) {
                    break;
                }

                //System.out.println("Sending request <" + message + "> to the shop...");

                writer.println(message); // send the message to the server

                String reply = reader.readLine(); // read the response from the server
                System.out.println(reply);
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
