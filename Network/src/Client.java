import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverHost = "localhost"; // Сървърът работи на локалния хост.
        int serverPort = 5555; // Порт, на който сървърът слуша.

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            List<Integer> data = new ArrayList<>(); // Входните данни за сортиране.

            Scanner scanner = new Scanner(System.in);
            for (int i = 0; i < 5; i++) {
                System.out.print("Element " + (i + 1) + ": ");
                data.add(scanner.nextInt());
            }

            out.writeObject(data); // Изпращаме входните данни на сървъра.

            @SuppressWarnings("unchecked")
            List<Integer> sortedData = (List<Integer>) in.readObject(); // Получаваме сортираните данни от сървъра.

            System.out.println("Sorted array: " + sortedData);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
