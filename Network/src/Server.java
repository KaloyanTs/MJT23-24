import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class Server {
    public static void main(String[] args) {
        final int portNumber = 5555;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server listens on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error on server: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;

    private static ExecutorService executor;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                List<Integer> data = (List<Integer>) in.readObject();
                Action action = (Action) in.readObject();

                List<Integer> res;

                String singleInfo = null;
                String multiInfo = null;

                switch (action) {
                    case Action.SINGLE_QSORT:
                        System.out.println("single");
                        res = quickSort(data);
                        break;
                    case Action.MULTI_QSORT:
                        System.out.println("multi");
                        //todo why not working????
                        executor = Executors.newFixedThreadPool(50);
                        res = parallelQuickSort(data);
                        executor.shutdown();
                        break;
                    case Action.BOTH:
                        System.out.println("both");
                        long startTime = System.currentTimeMillis();
                        quickSort(data);
                        long endTime = System.currentTimeMillis();
                        singleInfo = "Single threaded quicksort executed in " + (endTime - startTime) + "ms";
                        startTime = System.currentTimeMillis();
                        executor = Executors.newFixedThreadPool(50);
                        res = parallelQuickSort(data);
                        executor.shutdown();
                        endTime = System.currentTimeMillis();
                        multiInfo = "Multi threaded quicksort executed in " + (endTime - startTime) + "ms";
                        break;
                    default:
                        throw new IllegalArgumentException();
                }

                out.writeObject(res);
                if (singleInfo != null) {
                    out.writeObject(singleInfo);
                    out.writeObject(multiInfo);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error on Client handling: " + e.getMessage());
        }
    }

    private List<Integer> quickSort(List<Integer> data) {
        if (data.size() <= 1) {
            return data;
        }

        int pivot = data.get(data.size() / 2);
        List<Integer> less = data.stream().filter(x -> x < pivot).toList();
        List<Integer> equal = data.stream().filter(x -> x == pivot).toList();
        List<Integer> greater = data.stream().filter(x -> x > pivot).toList();

        //todo use smarter and faster function
        //todo Rearrange

        List<Integer> lessSorted = quickSort(less);
        List<Integer> greaterSorted = quickSort(greater);

        List<Integer> result = new ArrayList<>();
        result.addAll(lessSorted);
        result.addAll(equal);
        result.addAll(greaterSorted);

        return result;
    }

    private List<Integer> parallelQuickSort(List<Integer> data) {
        if (data.size() <= 1) {
            return data;
        }

        System.out.println("size: " + data.size());

        int pivot = data.get(data.size() / 2);
        List<Integer> less = data.stream().filter(x -> x < pivot).toList();
        List<Integer> equal = data.stream().filter(x -> x == pivot).toList();
        List<Integer> greater = data.stream().filter(x -> x > pivot).toList();

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

        return result;
    }
}
