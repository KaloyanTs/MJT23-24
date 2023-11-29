package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RideRightTest2 {

    RideRight rideRight;
    City BB, KA, AA, SZ, PJ, KZ, VT, TI, KU, TQ, KB;

    Journey BBKA, KAAA, AASZ, BBKZ, KZPJ, PJSZ, KATI, KZTI, BBTI, BBKU, KUTQ, BBKB, KBTQ;

    void initMyLargeGraph() {
        BB = new City("BB", new Location(260, 20));
        KA = new City("KA", new Location(260, 20));
        AA = new City("AA", new Location(260, 20));
        SZ = new City("SZ", new Location(260, 20));
        PJ = new City("PJ", new Location(260, 20));
        KZ = new City("KZ", new Location(260, 20));
        TI = new City("TI", new Location(260, 20));
        VT = new City("VT", new Location(260, 20));
        KU = new City("KU", new Location(260, 20));
        KB = new City("KB", new Location(260, 20));
        TQ = new City("TQ", new Location(260, 20));
        List<Journey> list = new ArrayList<>();

        BBKZ = new Journey(VehicleType.PLANE, BB, KZ, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(BBKZ);

        BBKA = new Journey(VehicleType.PLANE, BB, KA, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(BBKA);

        KZPJ = new Journey(VehicleType.PLANE, KZ, PJ, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(KZPJ);

        KAAA = new Journey(VehicleType.PLANE, KA, AA, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(KAAA);

        PJSZ = new Journey(VehicleType.PLANE, PJ, SZ, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(PJSZ);

        AASZ = new Journey(VehicleType.PLANE, AA, SZ, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(AASZ);

        KATI = new Journey(VehicleType.PLANE, KA, TI, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(KATI);

        KZTI = new Journey(VehicleType.PLANE, KZ, TI, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(KZTI);

        BBTI = new Journey(VehicleType.PLANE, BB, TI, BigDecimal.valueOf(20 * 500 * 400.0));
        list.add(BBTI);

        BBKU = new Journey(VehicleType.PLANE, BB, KU, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(BBKU);

        KUTQ = new Journey(VehicleType.PLANE, KU, TQ, BigDecimal.valueOf(2 * 40 * 40.0));
        list.add(KUTQ);

        BBKB = new Journey(VehicleType.PLANE, BB, KB, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(BBKB);

        KBTQ = new Journey(VehicleType.PLANE, KB, TQ, BigDecimal.valueOf(2 * 50 * 40.0));
        list.add(KBTQ);

        rideRight = new RideRight(list);
    }

    @Test
    void testMyLargeGraph() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(BB, SZ, true);

        SequencedCollection<Journey> test1 = List.of(BBKA, KAAA, AASZ);

        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph2() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(BB, SZ, false));
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(SZ, BB, true));
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(SZ, BB, false));

    }

    @Test
    void testMyLargeGraph3() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(SZ, VT,
            true));
        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(VT, SZ,
            true));
        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(SZ, VT,
            false));
        assertThrows(CityNotKnownException.class, () -> rideRight.findCheapestPath(VT, SZ,
            false));
        assertThrows(IllegalStateException.class, () -> rideRight.findCheapestPath(VT, VT,
            true));
    }

    @Test
    void testMyLargeGraph4() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(BB, TI, true);

        SequencedCollection<Journey> test1 = List.of(BBKA, KATI);

        assertEquals(l1, test1);
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TI, BB, false));
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TI, BB, true));

    }

    @Test
    void testMyLargeGraph5() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(BB, TI, false);

        SequencedCollection<Journey> test1 = List.of(BBTI);

        assertEquals(l1, test1);
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TI, BB, true));
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TI, BB, false));

    }

    @Test
    void testMyLargeGraph6() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(BB, TQ, true);

        SequencedCollection<Journey> test1 = List.of(BBKU, KUTQ);

        assertEquals(l1, test1);
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TQ, BB, false));
        assertThrows(NoPathToDestinationException.class, () -> rideRight.findCheapestPath(TQ, BB, false));

    }

}