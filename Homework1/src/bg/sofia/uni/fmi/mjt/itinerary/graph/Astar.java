package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.exception.GraphAlgorithmException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToVertexFoundException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.VertexNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SequencedCollection;
import java.util.Set;

public class Astar<V extends Comparable<V>, E extends WeightedEdge<V>> extends GraphAlgorithm<V, E> {

    private final Map<V, E> parent;
    private final Map<V, BigDecimal> distance;
    private final Set<V> visited;
    private SequencedCollection<E> path;

    private record VertexDistPair<V>(V vertex, BigDecimal distance) implements Comparable<VertexDistPair<V>> {
        @Override
        public int compareTo(VertexDistPair<V> other) {
            return this.distance().compareTo(other.distance());
        }
    }

    private class LeastWeightComparator implements Comparator<VertexDistPair<V>> {
        @Override
        public int compare(VertexDistPair<V> edge1, VertexDistPair<V> edge2) {
            return edge1.distance().compareTo(edge2.distance());
        }
    }

    public Astar() {
        parent = new HashMap<>();
        visited = new HashSet<>();
        distance = new HashMap<>();
    }

    private void initializeDistances(WeightedGraph<V, E> graph, V from) throws VertexNotFoundException {
        for (V vertex : graph.getVertices()) {
            distance.put(vertex, INFINITY);
        }
        if (distance.get(from) == null) {
            throw new VertexNotFoundException("Given vertex:" + from + " is not present in the graph...");
        }
        distance.put(from, new BigDecimal(0));
    }

    private SequencedCollection<E> restorePathToFrom(V endPoint, Map<V, E> parent) {
        SequencedCollection<E> res = new ArrayList<>();
        V iterVertex = endPoint;
        E iterEdge;
        //only edge is enough but not very readable
        while ((iterEdge = parent.get(iterVertex)) != null) {
            res.add(iterEdge);
            iterVertex = iterEdge.getFrom();
        }
        return res;
    }

    @Override
    void work(WeightedGraph<V, E> graph, AstarInput<V> input) throws GraphAlgorithmException {

        if (graph == null || input == null) {
            throw new IllegalArgumentException("Bad arguments given...");
        }

        if (!graph.getVertices().contains(input.getFrom()) || !graph.getVertices().contains(input.getTo())) {
            throw new VertexNotFoundException("One of the cities could not be found...");
        }

        //each vertex knows the edge who discovered it
        //for clearness parent of root is set to null
        parent.put(input.getFrom(), null);

        //priority queue (main logic)
        Queue<VertexDistPair<V>> queue =
            new PriorityQueue<>(new LeastWeightComparator());

        initializeDistances(graph, input.getFrom());

        visited.add(input.getFrom());
        queue.add(new VertexDistPair<>(input.getFrom(), new BigDecimal(0)));

        V current;
        while (!queue.isEmpty()) {
            current = queue.poll().vertex();

            if (current.equals(input.getTo())) {
                break;
            }

            visited.add(current);

            for (E edge : graph.getNeighbours(current)) {

                if (!visited.contains(edge.getTo()) && (distance.get(edge.getTo()).equals(GraphAlgorithm.INFINITY) ||

                    distance.get(current).add(edge.getWeight()).compareTo(distance.get(edge.getTo())) < 0)) {

                    //dist[u]+h(u)
                    queue.add(new VertexDistPair<>(edge.getTo(),
                        distance.get(current).add(edge.getWeight()).add(input.getHeuristicOf(edge.getTo()))));

                    distance.put(edge.getTo(), distance.get(current).add(edge.getWeight()));
                    parent.put(edge.getTo(), edge);
                }
            }
        }

        if (distance.get(input.getTo()) == INFINITY) {
            throw new NoPathToVertexFoundException(
                "No path from " + input.getFrom() + " to " + input.getTo() + " exists in this graph...");
        }

        path = restorePathToFrom(input.getTo(), parent);
        path = path.reversed();

    }

    public SequencedCollection<E> getPath() {
        if (!isRun) {
            throw new IllegalStateException("Algorithm not run yet");
        }
        return path;
    }
}
