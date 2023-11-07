import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client {
    static int serverPort = 5555;
    static int elementCount;
    static Action action;
    static Scanner scanner = new Scanner(System.in);

    static List<Integer> fill(int count) {
        List<Integer> res = new ArrayList<>();
        Random random = new Random();

        // Fill the array with random numbers
        for (int i = 0; i < count; i++) {
            res.add(random.nextInt(100));
        }
        return res;
    }

    static Action readReply() {
        Message.TYPES.print();
        String reply = scanner.nextLine();
        return switch (reply.toLowerCase()) {
            case "single" -> Action.SINGLE_QSORT;
            case "multi" -> Action.MULTI_QSORT;
            case "both" -> Action.BOTH;
            case "quit" -> Action.QUIT;
            default -> null;
        };
    }

    public static void main(String[] args) {

        String serverHost = "localhost";

        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {

            do {
                action = readReply();
            } while (action == null);

            if (action == Action.QUIT) {
                return;
            }

            List<Integer> data;

            Message.COUNT.print();

            elementCount = scanner.nextInt();

//            for (int i = 0; i < elementCount; i++) {
//                System.out.print("Element " + (i + 1) + ": ");
//                data.add(scanner.nextInt());
//            }

            data = fill(elementCount);

            out.writeObject(data);
            out.writeObject(action);

            @SuppressWarnings("unchecked")
            List<Integer> sortedData = (List<Integer>) in.readObject();
            System.out.println(sortedData);
            if (action == Action.BOTH) {
                String singleTime = (String) in.readObject();
                String multiTime = (String) in.readObject();
                System.out.println(singleTime);
                System.out.println(multiTime);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
