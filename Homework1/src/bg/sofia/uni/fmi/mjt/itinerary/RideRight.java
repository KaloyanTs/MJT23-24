package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.comparators.LexicalDestinationComparator;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.GraphAlgorithmException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToVertexFoundException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.VertexNotFoundException;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Astar;
import bg.sofia.uni.fmi.mjt.itinerary.graph.AstarInput;
import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {

    final private static BigDecimal AVERAGE_PRICE_KM = BigDecimal.valueOf(20);
    final private static BigDecimal M_PER_KM = BigDecimal.valueOf(1000);
    WeightedGraph<City, Journey> graph;

    public RideRight(List<Journey> schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Null given as list of journeys...");
        }
        graph = new WeightedGraph<>(schedule, new LexicalDestinationComparator());
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {

        if (start == null || destination == null) {
            throw new IllegalArgumentException("Null given as start or end city...");
        }

        if (start == destination) {
            throw new IllegalStateException("Start and end should not be the same...");
        }

        Map<City, BigDecimal> heuristic = new HashMap<>();
        for (City city : graph.getVertices()) {
            heuristic.put(city, city.distanceTo(start).divide(M_PER_KM).multiply(AVERAGE_PRICE_KM));
        }

        try {
            if (!allowTransfer) {
                return new ArrayList<>(Collections.singleton(graph.findLightestEdge(start, destination)));
            }
            Astar<City, Journey> res = new Astar<>();
            res.run(graph, new AstarInput<>(start, destination, heuristic));
            return res.getPath();
        } catch (VertexNotFoundException e) {
            throw new CityNotKnownException("Given city could not be found...", e);
        } catch (NoPathToVertexFoundException e) {
            throw new NoPathToDestinationException("No path to destination exists...", e);
        } catch (GraphAlgorithmException e) {
            throw new IllegalStateException("Unexpected exception has been thrown", e);
        }
    }
}