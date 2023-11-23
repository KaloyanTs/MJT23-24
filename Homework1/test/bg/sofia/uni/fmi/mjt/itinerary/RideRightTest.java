package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class RideRightTest {

    RideRight rideRight;
    City A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Z, A1, B1, C1, D1, E1, F1, G1, H1, I1, J1,
        K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1;

    Journey AD1, D1A, D1R, RD1, RB, BR, BB1, B1B, L1E, EL1, S1H1, H1S1, R1F, FR1, ZQ1, Q1Z, Q1M1, M1Q1, M1N, NM1, NK,
        KN, KP1, P1K, P1O, OP1, OC, CO, CN1, N1C, N1G1, G1N1, H1T1, T1H1, T1C1, C1T1, LT, TL, E1F1, F1E1, VI1, I1V,
        J1B1, B1J1, A1E, EA1, WH, HW, GJ, JG, UJ1, J1U, Q1D1, D1Q1, VR, RV, SO1, O1S, O1A1, A1O1, ZF, FZ, R1P1, P1R1,
        F1J, JF1, E1F, FE1, HM1, M1H, DO1, O1D, DM, MD, MA, AM, LN1, N1L, WS1, S1W, I1R1, R1I1;

    void initMyLargeGraph() {
        A = new City("A", new Location(20, 20));
        B = new City("B", new Location(180, 140));
        C = new City("C", new Location(120, 100));
        D = new City("D", new Location(100, 120));
        E = new City("E", new Location(80, 120));
        F = new City("F", new Location(40, 120));
        G = new City("G", new Location(60, 80));
        H = new City("H", new Location(100, 60));
        I = new City("I", new Location(140, 60));
        J = new City("J", new Location(180, 40));
        K = new City("K", new Location(200, 20));
        L = new City("L", new Location(100, 20));
        M = new City("M", new Location(60, 60));
        N = new City("N", new Location(20, 80));
        O = new City("O", new Location(80, 160));
        P = new City("P", new Location(100, 180));
        Q = new City("Q", new Location(140, 180));
        R = new City("R", new Location(140, 80));
        S = new City("S", new Location(160, 80));
        T = new City("T", new Location(160, 60));
        U = new City("U", new Location(200, 100));
        V = new City("V", new Location(120, 140));
        W = new City("W", new Location(40, 160));
        Z = new City("Z", new Location(20, 180));
        A1 = new City("A1", new Location(40, 200));
        B1 = new City("B1", new Location(80, 200));
        C1 = new City("C1", new Location(20, 60));
        D1 = new City("D1", new Location(60, 20));
        E1 = new City("E1", new Location(140, 20));
        F1 = new City("F1", new Location(160, 140));
        G1 = new City("G1", new Location(240, 120));
        H1 = new City("H1", new Location(240, 160));
        I1 = new City("I1", new Location(200, 200));
        J1 = new City("J1", new Location(220, 220));
        K1 = new City("K1", new Location(160, 200));
        L1 = new City("L1", new Location(120, 220));
        M1 = new City("M1", new Location(240, 60));
        N1 = new City("N1", new Location(300, 100));
        O1 = new City("O1", new Location(280, 160));
        P1 = new City("P1", new Location(260, 240));
        Q1 = new City("Q1", new Location(160, 240));
        R1 = new City("R1", new Location(60, 260));
        S1 = new City("S1", new Location(20, 240));
        T1 = new City("T1", new Location(260, 20));

        List<Journey> list = new ArrayList<>();

        AD1 = new Journey(VehicleType.BUS, A, D1, new BigDecimal(40));
        list.add(AD1);
        D1A = new Journey(VehicleType.BUS, D1, A, new BigDecimal(40));
        list.add(D1A);
        D1R = new Journey(VehicleType.BUS, D1, R, new BigDecimal(100));
        list.add(D1R);
        RD1 = new Journey(VehicleType.BUS, R, D1, new BigDecimal(100));
        list.add(RD1);
        RB = new Journey(VehicleType.BUS, R, B, new BigDecimal(72.11103));
        list.add(RB);
        BR = new Journey(VehicleType.BUS, B, R, new BigDecimal(72.11103));
        list.add(BR);
        BB1 = new Journey(VehicleType.BUS, B, B1, new BigDecimal(116.61904));
        list.add(BB1);
        B1B = new Journey(VehicleType.BUS, B1, B, new BigDecimal(116.61904));
        list.add(B1B);
        L1E = new Journey(VehicleType.BUS, L1, E, new BigDecimal(107.7033));
        list.add(L1E);
        EL1 = new Journey(VehicleType.BUS, E, L1, new BigDecimal(107.7033));
        list.add(EL1);
        S1H1 = new Journey(VehicleType.BUS, S1, H1, new BigDecimal(234.094));
        list.add(S1H1);
        H1S1 = new Journey(VehicleType.BUS, H1, S1, new BigDecimal(234.094));
        list.add(H1S1);
        R1F = new Journey(VehicleType.BUS, R1, F, new BigDecimal(141.42136));
        list.add(R1F);
        FR1 = new Journey(VehicleType.BUS, F, R1, new BigDecimal(141.42136));
        list.add(FR1);
        ZQ1 = new Journey(VehicleType.BUS, Z, Q1, new BigDecimal(152.31546));
        list.add(ZQ1);
        Q1Z = new Journey(VehicleType.BUS, Q1, Z, new BigDecimal(152.31546));
        list.add(Q1Z);
        Q1M1 = new Journey(VehicleType.BUS, Q1, M1, new BigDecimal(196.97716));
        list.add(Q1M1);
        M1Q1 = new Journey(VehicleType.BUS, M1, Q1, new BigDecimal(196.97716));
        list.add(M1Q1);
        M1N = new Journey(VehicleType.BUS, M1, N, new BigDecimal(220.90722));
        list.add(M1N);
        NM1 = new Journey(VehicleType.BUS, N, M1, new BigDecimal(220.90722));
        list.add(NM1);
        NK = new Journey(VehicleType.BUS, N, K, new BigDecimal(189.73666));
        list.add(NK);
        KN = new Journey(VehicleType.BUS, K, N, new BigDecimal(189.73666));
        list.add(KN);
        KP1 = new Journey(VehicleType.BUS, K, P1, new BigDecimal(228.03509));
        list.add(KP1);
        P1K = new Journey(VehicleType.BUS, P1, K, new BigDecimal(228.03509));
        list.add(P1K);
        P1O = new Journey(VehicleType.BUS, P1, O, new BigDecimal(196.97716));
        list.add(P1O);
        OP1 = new Journey(VehicleType.BUS, O, P1, new BigDecimal(196.97716));
        list.add(OP1);
        OC = new Journey(VehicleType.BUS, O, C, new BigDecimal(72.11103));
        list.add(OC);
        CO = new Journey(VehicleType.BUS, C, O, new BigDecimal(72.11103));
        list.add(CO);
        CN1 = new Journey(VehicleType.BUS, C, N1, new BigDecimal(180));
        list.add(CN1);
        N1C = new Journey(VehicleType.BUS, N1, C, new BigDecimal(180));
        list.add(N1C);
        N1G1 = new Journey(VehicleType.BUS, N1, G1, new BigDecimal(63.24555));
        list.add(N1G1);
        G1N1 = new Journey(VehicleType.BUS, G1, N1, new BigDecimal(63.24555));
        list.add(G1N1);
        H1T1 = new Journey(VehicleType.BUS, H1, T1, new BigDecimal(141.42136));
        list.add(H1T1);
        T1H1 = new Journey(VehicleType.BUS, T1, H1, new BigDecimal(141.42136));
        list.add(T1H1);
        T1C1 = new Journey(VehicleType.BUS, T1, C1, new BigDecimal(243.3105));
        list.add(T1C1);
        C1T1 = new Journey(VehicleType.BUS, C1, T1, new BigDecimal(243.3105));
        list.add(C1T1);
        LT = new Journey(VehicleType.BUS, L, T, new BigDecimal(72.11103));
        list.add(LT);
        TL = new Journey(VehicleType.BUS, T, L, new BigDecimal(72.11103));
        list.add(TL);
        E1F1 = new Journey(VehicleType.BUS, E1, F1, new BigDecimal(121.65525));
        list.add(E1F1);
        F1E1 = new Journey(VehicleType.BUS, F1, E1, new BigDecimal(121.65525));
        list.add(F1E1);
        VI1 = new Journey(VehicleType.BUS, V, I1, new BigDecimal(100));
        list.add(VI1);
        I1V = new Journey(VehicleType.BUS, I1, V, new BigDecimal(100));
        list.add(I1V);
        J1B1 = new Journey(VehicleType.BUS, J1, B1, new BigDecimal(141.42136));
        list.add(J1B1);
        B1J1 = new Journey(VehicleType.BUS, B1, J1, new BigDecimal(141.42136));
        list.add(B1J1);
        A1E = new Journey(VehicleType.BUS, A1, E, new BigDecimal(89.44272));
        list.add(A1E);
        EA1 = new Journey(VehicleType.BUS, E, A1, new BigDecimal(89.44272));
        list.add(EA1);
        WH = new Journey(VehicleType.BUS, W, H, new BigDecimal(116.61904));
        list.add(WH);
        HW = new Journey(VehicleType.BUS, H, W, new BigDecimal(116.61904));
        list.add(HW);
        GJ = new Journey(VehicleType.BUS, G, J, new BigDecimal(126.49111));
        list.add(GJ);
        JG = new Journey(VehicleType.BUS, J, G, new BigDecimal(126.49111));
        list.add(JG);
        UJ1 = new Journey(VehicleType.BUS, U, J1, new BigDecimal(121.65525));
        list.add(UJ1);
        J1U = new Journey(VehicleType.BUS, J1, U, new BigDecimal(121.65525));
        list.add(J1U);
        Q1D1 = new Journey(VehicleType.BUS, Q1, D1, new BigDecimal(241.66092));
        list.add(Q1D1);
        D1Q1 = new Journey(VehicleType.BUS, D1, Q1, new BigDecimal(241.66092));
        list.add(D1Q1);
        VR = new Journey(VehicleType.BUS, V, R, new BigDecimal(63.24555));
        list.add(VR);
        RV = new Journey(VehicleType.BUS, R, V, new BigDecimal(63.24555));
        list.add(RV);
        SO1 = new Journey(VehicleType.BUS, S, O1, new BigDecimal(144.22205));
        list.add(SO1);
        O1S = new Journey(VehicleType.BUS, O1, S, new BigDecimal(144.22205));
        list.add(O1S);
        O1A1 = new Journey(VehicleType.BUS, O1, A1, new BigDecimal(243.3105));
        list.add(O1A1);
        A1O1 = new Journey(VehicleType.BUS, A1, O1, new BigDecimal(243.3105));
        list.add(A1O1);
        ZF = new Journey(VehicleType.BUS, Z, F, new BigDecimal(63.24555));
        list.add(ZF);
        FZ = new Journey(VehicleType.BUS, F, Z, new BigDecimal(63.24555));
        list.add(FZ);
        R1P1 = new Journey(VehicleType.BUS, R1, P1, new BigDecimal(200.99751));
        list.add(R1P1);
        P1R1 = new Journey(VehicleType.BUS, P1, R1, new BigDecimal(200.99751));
        list.add(P1R1);
        F1J = new Journey(VehicleType.BUS, F1, J, new BigDecimal(101.98039));
        list.add(F1J);
        JF1 = new Journey(VehicleType.BUS, J, F1, new BigDecimal(101.98039));
        list.add(JF1);
        E1F = new Journey(VehicleType.BUS, E1, F, new BigDecimal(141.42136));
        list.add(E1F);
        FE1 = new Journey(VehicleType.BUS, F, E1, new BigDecimal(141.42136));
        list.add(FE1);
        HM1 = new Journey(VehicleType.BUS, H, M1, new BigDecimal(140));
        list.add(HM1);
        M1H = new Journey(VehicleType.BUS, M1, H, new BigDecimal(140));
        list.add(M1H);
        DO1 = new Journey(VehicleType.BUS, D, O1, new BigDecimal(184.39089));
        list.add(DO1);
        O1D = new Journey(VehicleType.BUS, O1, D, new BigDecimal(184.39089));
        list.add(O1D);
        DM = new Journey(VehicleType.BUS, D, M, new BigDecimal(72.11103));
        list.add(DM);
        MD = new Journey(VehicleType.BUS, M, D, new BigDecimal(72.11103));
        list.add(MD);
        MA = new Journey(VehicleType.BUS, M, A, new BigDecimal(56.568));
        list.add(MA);
        AM = new Journey(VehicleType.BUS, A, M, new BigDecimal(56.568));
        list.add(AM);
        LN1 = new Journey(VehicleType.BUS, L, N1, new BigDecimal(215.406));
        list.add(LN1);
        N1L = new Journey(VehicleType.BUS, N1, L, new BigDecimal(215.406));
        list.add(N1L);
        WS1 = new Journey(VehicleType.BUS, W, S1, new BigDecimal(82.462));
        list.add(WS1);
        S1W = new Journey(VehicleType.BUS, S1, W, new BigDecimal(82.462));
        list.add(S1W);
        I1R1 = new Journey(VehicleType.BUS, I1, R1, new BigDecimal(152.315));
        list.add(I1R1);
        R1I1 = new Journey(VehicleType.BUS, R1, I1, new BigDecimal(152.315));
        list.add(R1I1);
        rideRight = new RideRight(list);
    }

    @Test
    void testMyLargeGraph() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l1 = rideRight.findCheapestPath(F, T1, true);

        SequencedCollection<Journey> test1 = List.of(FZ, ZQ1, Q1M1, M1H, HW, WS1, S1H1, H1T1);

        Assertions.assertEquals(l1, test1);
    }

    @Test
    void testMyLargeGraph2() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();

        SequencedCollection<Journey> l2 = rideRight.findCheapestPath(A, O1, true);

        SequencedCollection<Journey> test2 = List.of(AM, MD, DO1);

        Assertions.assertEquals(l2, test2);
    }

    @Test
    void testMyLargeGraph3() throws CityNotKnownException, NoPathToDestinationException {
        initMyLargeGraph();


        SequencedCollection<Journey> l3 = rideRight.findCheapestPath(N, P1, true);

        SequencedCollection<Journey> test3 = List.of(NK, KP1);

        Assertions.assertEquals(l3, test3);
    }
}
