package bg.sofia.uni.fmi.mjt.itinerary.graph;

import java.math.BigDecimal;
import java.util.Map;

public class AstarInput<V extends Comparable<V>> extends AlgorithmInput {

    final V from;
    final V to;
    final Map<V, BigDecimal> heuristic;

    public AstarInput(V from, V to, Map<V, BigDecimal> heuristic) {
        this.from = from;
        this.to = to;
        this.heuristic = heuristic;
    }

    public V getFrom() {
        return from;
    }

    public V getTo() {
        return to;
    }

    public BigDecimal getHeuristicOf(V vertex) {
        return heuristic.get(vertex);
    }

}
