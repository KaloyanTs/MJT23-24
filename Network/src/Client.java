import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    static int serverPort = 5555;
    static int elementCount;
    static ACTION action;
    static Scanner scanner = new Scanner(System.in);

    static ACTION readReply() {
        MESSAGE.TYPES.print();
        String reply = scanner.nextLine();
        return switch (reply.toLowerCase()) {
            case "single" -> ACTION.SINGLE_QSORT;
            case "multi" -> ACTION.MULTI_QSORT;
            case "both" -> ACTION.BOTH;
            default -> null;
        };
    }

    public static void main(String[] args) {

        String serverHost = "localhost";

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            List<Integer> data = new ArrayList<>();

            MESSAGE.COUNT.print();

            elementCount = scanner.nextInt();

            for (int i = 0; i < elementCount; i++) {
                System.out.print("Element " + (i + 1) + ": ");
                data.add(scanner.nextInt());
            }

            while ((action = readReply()) == null) {
            }

            out.writeObject(new InputData(data, action));

            @SuppressWarnings("unchecked")
            List<Integer> sortedData = (List<Integer>) in.readObject();

            System.out.println("Sorted array: " + sortedData);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
