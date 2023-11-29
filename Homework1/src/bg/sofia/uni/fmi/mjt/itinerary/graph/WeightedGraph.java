package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToVertexFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class WeightedGraph<V extends Comparable<V>, E extends WeightedEdge<V> & Comparable<E>> {

    Map<V, SortedSet<E>> graph;

    public WeightedGraph(List<? extends E> adjacencyList) {
        graph = new HashMap<>();
        for (E e : adjacencyList) {
            addEdge(e);
        }
    }

    public Set<V> getVertices() {
        return graph.keySet();
    }

    public SortedSet<E> getNeighbours(V vertex) {
        return graph.get(vertex);
    }

    public void addEdge(E edge) {
        if (graph.get(edge.getFrom()) == null) {
            graph.put(edge.getFrom(), new TreeSet<>());
        }
        graph.get(edge.getFrom()).add(edge);
    }

    public E findLightestEdge(V from, V to) throws NoPathToVertexFoundException {
        E lightest = null;
        for (E edge : graph.get(from)) {
            if (edge.getTo() == to) {
                if (lightest == null || edge.getWeight().compareTo(lightest.getWeight()) < 0) {
                    lightest = edge;
                }
            }
        }
        if (lightest == null) {
            throw new NoPathToVertexFoundException("No direct edge from " + from + " to " + to + " found...");
        }
        return lightest;
    }
}
