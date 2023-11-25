package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.GraphAlgorithmException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToVertexFoundException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.VertexNotFoundException;
import bg.sofia.uni.fmi.mjt.itinerary.graph.Astar;
import bg.sofia.uni.fmi.mjt.itinerary.graph.AstarInput;
import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraph;

import javax.lang.model.type.UnknownTypeException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {

    WeightedGraph<City, Journey> graph;

    public RideRight(List<Journey> schedule) {
        graph = new WeightedGraph<>(schedule);
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {

        Map<City, BigDecimal> heuristic = new HashMap<>();
        for (City city : graph.getVertices()) {
            heuristic.put(city, city.distanceTo(start));
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