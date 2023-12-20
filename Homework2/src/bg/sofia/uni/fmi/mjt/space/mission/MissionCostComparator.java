package bg.sofia.uni.fmi.mjt.space.mission;

import java.util.Comparator;

public class MissionCostComparator implements Comparator<Mission> {

    boolean increasing;

    public MissionCostComparator(boolean increasing) {
        this.increasing = increasing;
    }

    @Override
    public int compare(Mission m1, Mission m2) {
        if (!m1.cost().isPresent()) return 1;
        if (!m2.cost().isPresent()) return -1;
        return (increasing ? (int) (m1.cost().get() - m2.cost().get()) : (int) (m2.cost().get() - m1.cost().get()));
    }
}
