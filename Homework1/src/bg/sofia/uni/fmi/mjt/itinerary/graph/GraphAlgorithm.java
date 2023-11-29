package bg.sofia.uni.fmi.mjt.itinerary.graph;

import bg.sofia.uni.fmi.mjt.itinerary.exception.GraphAlgorithmException;

import java.math.BigDecimal;

public abstract class GraphAlgorithm<V extends Comparable<V>, E extends Comparable<E> & WeightedEdge<V>> {

    boolean isRun;
    protected final static BigDecimal INFINITY = new BigDecimal(-1);

    GraphAlgorithm() {
        isRun = false;
    }

    abstract void work(WeightedGraph<V, E> graph, AstarInput<V> input) throws GraphAlgorithmException;

    public void run(WeightedGraph<V, E> graph, AstarInput<V> input) throws GraphAlgorithmException {
        if (isRun) {
            throw new UnsupportedOperationException("GraphAlgorithm class can be used only once in its lifetime...");
        }
        work(graph, input);
        isRun = true;
    }
}
