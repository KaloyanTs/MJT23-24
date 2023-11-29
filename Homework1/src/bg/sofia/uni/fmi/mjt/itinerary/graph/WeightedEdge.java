package bg.sofia.uni.fmi.mjt.itinerary.graph;

import java.math.BigDecimal;

public interface WeightedEdge<VertexInterpretation> {

    VertexInterpretation getFrom();

    VertexInterpretation getTo();

    BigDecimal getWeight();
}
