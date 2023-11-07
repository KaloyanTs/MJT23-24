import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    static int serverPort = 5555;

    public static void main(String[] args) {

        String serverHost = "localhost";

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            List<Integer> data = new ArrayList<>();

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter count of elements: ");

            int n = scanner.nextInt();

            for (int i = 0; i < n; i++) {
                System.out.print("Element " + (i + 1) + ": ");
                data.add(scanner.nextInt());
            }

            out.writeObject(data);

            @SuppressWarnings("unchecked")
            List<Integer> sortedData = (List<Integer>) in.readObject();

            System.out.println("Sorted array: " + sortedData);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
