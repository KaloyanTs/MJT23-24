package bg.sofia.uni.fmi.mjt.itinerary;

import java.math.BigDecimal;

public record Location(int x, int y) {

    public BigDecimal distanceTo(Location other) {
        return new BigDecimal(Math.abs(x - other.x) + Math.abs(y - other.y));
    }
}
