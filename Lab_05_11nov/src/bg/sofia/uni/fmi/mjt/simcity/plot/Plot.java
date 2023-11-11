package bg.sofia.uni.fmi.mjt.simcity.plot;

import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.simcity.exception.BuildableNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.InsufficientPlotAreaException;
import bg.sofia.uni.fmi.mjt.simcity.property.buildable.Buildable;

import java.util.HashMap;
import java.util.Map;

public class Plot<E extends Buildable> implements PlotAPI<E> {

    private int buildableArea;

    private Map<String, E> area;

    public Plot(int buildableArea) {
        this.buildableArea = buildableArea;
        this.area = new HashMap<>();
    }

    @Override
    public void construct(String address, E buildable) {
        if (address == null || address.isBlank() || buildable == null) {
            throw new IllegalArgumentException();
        }
        if (area.get(address) != null) {
            throw new BuildableAlreadyExistsException("The address is already take...");
        }
        if (buildable.getArea() > buildableArea) {
            throw new InsufficientPlotAreaException("Cannot construct on this much area...");
        }
        buildableArea -= buildable.getArea();
        area.put(address, buildable);
    }

    @Override
    public void demolish(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (area.get(address) == null) {
            throw new BuildableNotFoundException("No buildable found on the given address...");
        }
        buildableArea += area.get(address).getArea();
        area.remove(address);
    }

    @Override
    public void demolishAll() {
        for (String address : area.keySet()) {
            buildableArea += area.get(address).getArea();
        }
        area = new HashMap<>();
    }

    @Override
    public Map<String, E> getAllBuildables() {
        return Map.copyOf(area);
    }

    @Override
    public int getRemainingBuildableArea() {
        return buildableArea;
    }

    @Override
    public void constructAll(Map<String, E> buildables) {
        if (buildables == null || buildables.isEmpty()) {
            throw new IllegalArgumentException("Map must not be null or empty...");
        }
        double sumArea = 0;
        for (Map.Entry<String, E> e : buildables.entrySet()) {
            if (area.get(e.getKey()) != null) {
                throw new BuildableAlreadyExistsException("There is an  already taken address...");
            }
            sumArea += e.getValue().getArea();
        }
        if (sumArea > buildableArea) {
            throw new InsufficientPlotAreaException("Not enough area for all the buildings...");
        }
        for (Map.Entry<String, E> e : buildables.entrySet()) {
            construct(e.getKey(),
                e.getValue());
        }
    }
}
