import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class BFSWorker extends Thread {

    private final Map<String, Set<String>> graph;
    private final BlockingQueue<Pair<String, Integer>> queue;
    private final Set<String> visited;
    private final int myGroupCount;

    private final Map<Integer, Set<String>> layers;

    public BFSWorker(BlockingQueue<Pair<String, Integer>> queue, Set<String> visited,
                     Map<String, Set<String>> graph, Map<Integer, Set<String>> layers, int myGroupCount) {
        this.queue = queue;
        this.visited = visited;
        this.graph = graph;
        this.layers = layers;
        this.myGroupCount = myGroupCount;
    }

    @Override
    public void run() {
        Pair<String, Integer> p = null;
        while (visited.size() < graph.keySet().size()) {
            try {
                p = queue.take();
                if (p.second() == -1) {
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assert p != null;
            visited.add(p.first());
            layers.computeIfAbsent(p.second(), k -> new HashSet<>());
            layers.get(p.second()).add(p.first());

            if (visited.size() == graph.keySet().size()) {
                //all vertices visited, telling other threads to stop with a poison object
                for (int i = 0; i < myGroupCount - 1; i++) {
                    try {
                        queue.put(new Pair<>(null, -1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            for (String neighbour : graph.get(p.first())) {
                if (!visited.contains(neighbour)) {
                    try {
                        queue.put(new Pair<>(neighbour, p.second() + 1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
