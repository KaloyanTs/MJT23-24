package bg.sofia.uni.fmi.mjt.itinerary.comparators;

import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.util.Comparator;

public class LexicalDestinationComparator implements Comparator<Journey> {
    @Override
    public int compare(Journey j1, Journey j2) {
        return j1.getTo().compareTo(j2.getTo());
    }
}
