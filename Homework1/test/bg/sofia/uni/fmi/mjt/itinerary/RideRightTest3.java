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

public class RideRightTest3 {

    RideRight rideRight;
    City A, B, C, D, E, F, G, H, I;

    Journey AB, AH, BH, BC, GH, HI, CI, GI, CF, GF, DF, DE, EF, CD;
    Journey BA, HA, HB, CB, HG, IH, IC, IG, FC, FG, FD, ED, FE, DC;
    void initMyLargeGraph() {
        A = new City("0", new Location(260, 20));
        B = new City("1", new Location(260, 20));
        C = new City("2", new Location(260, 20));
        D = new City("3", new Location(260, 20));
        E = new City("4", new Location(260, 20));
        F = new City("5", new Location(260, 20));
        G = new City("6", new Location(260, 20));
        H = new City("7", new Location(260, 20));
        I = new City("8", new Location(260, 20));
        List<Journey> list = new ArrayList<>();

        AB = new Journey(VehicleType.PLANE, A, B, BigDecimal.valueOf(4));
        list.add(AB);

        AH = new Journey(VehicleType.PLANE, A, H, BigDecimal.valueOf(8));
        list.add(AH);

        BH = new Journey(VehicleType.PLANE, B, H, BigDecimal.valueOf(11));
        list.add(BH);

        BC = new Journey(VehicleType.PLANE, B, C, BigDecimal.valueOf(8));
        list.add(BC);

        DC = new Journey(VehicleType.PLANE, D, C, BigDecimal.valueOf(7));
        list.add(DC);

        CD = new Journey(VehicleType.PLANE, C, D, BigDecimal.valueOf(7));
        list.add(CD);

        GH = new Journey(VehicleType.PLANE, G, H, BigDecimal.valueOf(1));
        list.add(GH);

        HI = new Journey(VehicleType.PLANE, H, I, BigDecimal.valueOf(7));
        list.add(HI);

        CI = new Journey(VehicleType.PLANE, C, I, BigDecimal.valueOf(2));
        list.add(CI);

        GI = new Journey(VehicleType.PLANE, G, I, BigDecimal.valueOf(6));
        list.add(GI);

        CF = new Journey(VehicleType.PLANE, C, F, BigDecimal.valueOf(4));
        list.add(CF);

        GF = new Journey(VehicleType.PLANE, G, F, BigDecimal.valueOf(2));
        list.add(GF);

        DF = new Journey(VehicleType.TRAIN, D, F, BigDecimal.valueOf(14));
        list.add(DF);

        DE = new Journey(VehicleType.PLANE, D, E, BigDecimal.valueOf(9));
        list.add(DE);

        EF = new Journey(VehicleType.PLANE, E, F, BigDecimal.valueOf(10));
        list.add(EF);

        BA = new Journey(VehicleType.PLANE, B, A, BigDecimal.valueOf(4));
        list.add(BA);

        HA = new Journey(VehicleType.PLANE, H, A, BigDecimal.valueOf(8));
        list.add(HA);

        HB = new Journey(VehicleType.PLANE, H, B, BigDecimal.valueOf(11));
        list.add(HB);

        CB = new Journey(VehicleType.PLANE, C, B, BigDecimal.valueOf(8));
        list.add(CB);

        HG = new Journey(VehicleType.PLANE, H, G, BigDecimal.valueOf(1));
        list.add(HG);

        IH = new Journey(VehicleType.PLANE, I, H, BigDecimal.valueOf(7));
        list.add(IH);

        IC = new Journey(VehicleType.PLANE, I, C, BigDecimal.valueOf(2));
        list.add(IC);

        IG = new Journey(VehicleType.PLANE, I, G, BigDecimal.valueOf(6));
        list.add(IG);

        FC = new Journey(VehicleType.PLANE, F, C, BigDecimal.valueOf(4));
        list.add(FC);

        FG = new Journey(VehicleType.PLANE, F, G, BigDecimal.valueOf(2));
        list.add(FG);

        FD = new Journey(VehicleType.TRAIN, F, D, BigDecimal.valueOf(14));
        list.add(FD);

        ED = new Journey(VehicleType.PLANE, E, D, BigDecimal.valueOf(9));
        list.add(ED);

        FE = new Journey(VehicleType.PLANE, F, E, BigDecimal.valueOf(10));
        list.add(FE);

        rideRight = new RideRight(list);
    }

    @Test
    void testMyLargeGraph() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, B, true);
        SequencedCollection<Journey> test1 = List.of(AB);
        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph2() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, C, true);
        SequencedCollection<Journey> test1 = List.of(AB, BC);
        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph3() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, D, true);
        SequencedCollection<Journey> test1 = List.of(AB, BC, CD);
        assertEquals(l1, test1);
    }
    @Test
    void testMyLargeGraph4() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, E, true);
        SequencedCollection<Journey> test1 = List.of(AH, HG, GF, FE);
        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph5() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, F, true);
        SequencedCollection<Journey> test1 = List.of(AH, HG, GF);
        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph6() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, G, true);
        SequencedCollection<Journey> test1 = List.of(AH, HG);
        assertEquals(l1, test1);
    }
    @Test
    void testMyLargeGraph7() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, H, true);
        SequencedCollection<Journey> test1 = List.of(AH);
        assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph8() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(A, I, true);
        SequencedCollection<Journey> test1 = List.of(AB, BC, CI);
        assertEquals(l1, test1);
    }
}