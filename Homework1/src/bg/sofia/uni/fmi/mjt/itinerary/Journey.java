package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedEdge;
import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) implements WeightedEdge<City>,
    Comparable<Journey> {

    public Journey {
        if (vehicleType == null || from == null || to == null || price == null) {
            throw new IllegalArgumentException("Null given as argument...");
        }
    }

    @Override
    public City getFrom() {
        return from;
    }

    @Override
    public City getTo() {
        return to;
    }

    @Override
    public BigDecimal getWeight() {
        return (vehicleType.getGreenTax().add(BigDecimal.valueOf(1))).multiply(price);
    }

    @Override
    public int compareTo(Journey other) {
        return getWeight().compareTo(other.getWeight());
    }
}