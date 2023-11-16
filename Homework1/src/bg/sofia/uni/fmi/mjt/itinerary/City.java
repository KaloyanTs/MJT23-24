package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;

public record City(String name, Location location) implements Comparable<City> {

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().getTypeName().equals(obj.getClass().getTypeName()) &&
            hashCode() == obj.hashCode();
    }

    @Override
    public int compareTo(City other) {
        return name.compareTo(other.name);
    }

    public BigDecimal distanceTo(City other) {
        return location.distanceTo(other.location);
    }

}
