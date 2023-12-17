import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class ClientRequestHandler implements Runnable {

    private final Socket socket;
    private final Map<String, Set<String>> graph;
    private Map<Integer, Set<String>> layers;
    private ConcurrentSkipListSet<String> visited;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
        graph = new HashMap<>();
        layers = null;
        visited = null;
    }

    private double bfs(String from, int threadCount) throws InterruptedException {
        long timeBegin = System.nanoTime();

        layers = new HashMap<>();
        visited = new ConcurrentSkipListSet<>();

        if (graph.get(from) == null) {
            return (System.nanoTime() - timeBegin) / 1_000_000_000;
        }

        BlockingQueue<Pair<String, Integer>> queue = new ArrayBlockingQueue<>(10);

        queue.put(new Pair<>(from, 0));

        System.out.println();

        List<BFSWorker> workerList = new ArrayList<>();

        BFSWorker w;
        for (int i = 0; i < threadCount; i++) {
            w = new BFSWorker(queue, visited, graph, layers, threadCount);
            workerList.add(w);
            w.start();
        }

        for (BFSWorker worker : workerList) {
            worker.join();
            System.out.println(worker.getName() + " ready.");
        }
        return (double) (System.nanoTime() - timeBegin) / 1_000_000_000;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("Client Request Handler for " + socket.getRemoteSocketAddress());

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // autoflush on
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            int count = Integer.parseInt(in.readLine());

            String inputLine;
            String[] input;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("exit")) {
                    return;
                }
                if (inputLine.contains(" ")) {
                    input = inputLine.split(" ");
                    graph.computeIfAbsent(input[0], k -> new HashSet<>());
                    graph.computeIfAbsent(input[1], k -> new HashSet<>());
                    graph.get(input[0]).add(input[1]);
                    graph.get(input[1]).add(input[0]);
                } else {
                    double time = bfs(inputLine, count);
                    for (Integer layer : layers.keySet()) {
                        out.println("On layer " + layer + ":");
                        for (String v : layers.get(layer)) {
                            out.println("\t".repeat(layer) + v);
                        }
                    }
                    out.println("Time for execution: " + time + "s.");
                    graph.clear();
                }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
