package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Comparator;

public class RocketBiggestHeightComparator implements Comparator<Rocket> {
    @Override
    public int compare(Rocket r1, Rocket r2) {
        if (!r1.height().isPresent()) return 1;
        if (!r2.height().isPresent()) return -1;
        return (int) (r2.height().get() - r1.height().get());
    }
}
