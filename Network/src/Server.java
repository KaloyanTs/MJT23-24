import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args) {
        int portNumber = 5555; // Порт, на който сървърът ще слуша.

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server listens on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Изчакваме клиентска връзка.

                // Създаваме отделна нишка за обслужване на връзката.
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error on server: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            @SuppressWarnings("unchecked")
            List<Integer> data = (List<Integer>) in.readObject(); // Получаваме входните данни от клиента.

            // Използваме паралелен quicksort за сортиране на данните.
            List<Integer> sortedData = parallelQuickSort(data);

            out.writeObject(sortedData); // Изпращаме сортираните данни обратно на клиента.
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Грешка при обслужване на клиент: " + e.getMessage());
        }
    }

    private List<Integer> parallelQuickSort(List<Integer> data) {
        if (data.size() <= 1) {
            return data;
        }

        int pivot = data.get(data.size() / 2);
        List<Integer> less = data.stream().filter(x -> x < pivot).collect(Collectors.toList());
        List<Integer> equal = data.stream().filter(x -> x == pivot).collect(Collectors.toList());
        List<Integer> greater = data.stream().filter(x -> x > pivot).collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<List<Integer>> lessSorted = executor.submit(() -> parallelQuickSort(less));
        Future<List<Integer>> greaterSorted = executor.submit(() -> parallelQuickSort(greater));

        List<Integer> result = new ArrayList<>();

        try {
            result.addAll(lessSorted.get());
            result.addAll(equal);
            result.addAll(greaterSorted.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        return result;
    }
}
