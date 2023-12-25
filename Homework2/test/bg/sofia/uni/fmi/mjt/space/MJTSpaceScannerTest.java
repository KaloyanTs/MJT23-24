package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MJTSpaceScannerTest {

    SpaceScannerAPI scanner;

    @BeforeEach
    void init() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        scanner =
            new MJTSpaceScanner(
                new StringReader("""
                    Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
                    0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
                    1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Failure
                    2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
                    3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
                    4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Tsyklon-3 | Perseverance,StatusActive,"145.0 ",Success
                    """),
                new StringReader("""
                    "",Name,Wiki,Rocket Height
                    0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
                    1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
                    2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
                    3,Unha-3,,32.0 m
                    4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m
                    """),
                kgen.generateKey());
    }

    @Test
    void testMJTSpaceScannerCreation() {
        assertEquals(5, scanner.getAllMissions().size());
        assertEquals(5, scanner.getAllRockets().size());
    }

    @Test
    void testGetAllMissionsByStatus() throws FileNotFoundException, NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SpaceScannerAPI space = new MJTSpaceScanner(new FileReader(Path.of("D:\\JavaProjects\\Homework2\\all-missions" +
            "-from-1957.csv").toFile()), new FileReader(Path.of("D:\\JavaProjects\\Homework2\\all-rockets" +
            "-from-1957.csv").toFile()), kgen.generateKey());

        Collection<Mission> res = space.getAllMissions(MissionStatus.SUCCESS);
        for (Mission m : res) {
            assertEquals(MissionStatus.SUCCESS, m.missionStatus());
        }
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissions() throws FileNotFoundException, NoSuchAlgorithmException {
        assertEquals("SpaceX",
            scanner.getCompanyWithMostSuccessfulMissions(
                LocalDate.now().minusYears(200),
                LocalDate.now()
            )
        );
        assertThrows(TimeFrameMismatchException.class,
            () -> scanner.getCompanyWithMostSuccessfulMissions(
                LocalDate.now(),
                LocalDate.now().minusYears(200)
            )
        );
    }

    @Test
    void testGetMissionsPerCountry() {
        Map<String, Collection<Mission>> grouped = scanner.getMissionsPerCountry();

        for (Map.Entry<String, Collection<Mission>> e : grouped.entrySet()) {
            for (Mission m : e.getValue())
                assertEquals(e.getKey(), m.location());
        }
    }

    @Test
    void testGetTopNLeastExpensiveMissions() {
        List<Mission> cheap = scanner.getTopNLeastExpensiveMissions(1, MissionStatus.SUCCESS,
            RocketStatus.STATUS_ACTIVE);

        assertEquals(1, cheap.size());
        assertEquals("SpaceX", cheap.getFirst().company());

        cheap = scanner.getTopNLeastExpensiveMissions(10, MissionStatus.SUCCESS,
            RocketStatus.STATUS_ACTIVE);
        assertEquals(4, cheap.size());
    }

    @Test
    void getMostDesiredLocationForMissionsPerCompany() {
        Map<String, String> companyLocation = scanner.getMostDesiredLocationForMissionsPerCompany();
        assertTrue(companyLocation.get("SpaceX").contains("USA"));
        assertTrue(companyLocation.get("CASC").contains("China"));
        assertTrue(companyLocation.get("ULA").contains("USA"));
        assertTrue(companyLocation.get("Roscosmos").contains("Kazakhstan"));
        assertNull(companyLocation.get("IAI"));
    }

    @Test
    void testGetAllRockets() {
        Collection<Rocket> rockets = scanner.getAllRockets();
        assertEquals(5, rockets.size());
    }

    @Test
    void getWikiPageForRocket() {
        Map<String, Optional<String>> wikis = scanner.getWikiPageForRocket();
        assertEquals(Optional.empty(), wikis.get("Unha-3"));
    }

    @Test
    void testGetTopNTallestRockets() {
        Collection<Rocket> rockets = scanner.getTopNTallestRockets(2);
        for (Rocket r : rockets) {
            assertTrue(r.name().contains("Tsyklon"));
        }
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissions() {
        Collection<String> rockets = scanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(2,
            MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);
        assertTrue(rockets.contains("https://en.wikipedia.org/wiki/Tsyklon-3"));
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompany() {
        Map<String, String> data =
            scanner.getLocationWithMostSuccessfulMissionsPerCompany(
                LocalDate.now().minusYears(200),
                LocalDate.now()
            );
        assertTrue(data.get("SpaceX").contains("USA"));
        assertEquals("", data.get("CASC"));
        assertTrue(data.get("ULA").contains("USA"));
        assertTrue(data.get("Roscosmos").contains("Kazakhstan"));
        assertNull(data.get("IAI"));

        assertThrows(TimeFrameMismatchException.class, () -> scanner.getLocationWithMostSuccessfulMissionsPerCompany(
            LocalDate.now(),
            LocalDate.now().minusYears(200)
        ));
    }

    @Test
    void testSaveMostReliableRocket() throws CipherException {
        OutputStream outputStream = new ByteArrayOutputStream();
        scanner.saveMostReliableRocket(outputStream, LocalDate.now().minusYears(200), LocalDate.now());
        assertThrows(TimeFrameMismatchException.class,
            () -> scanner.saveMostReliableRocket(outputStream,
                LocalDate.now(), LocalDate.now().minusYears(1)));
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(null, LocalDate.now(), LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(outputStream, null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(outputStream, LocalDate.now(), null));
    }
}
