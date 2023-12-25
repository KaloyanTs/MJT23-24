package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                    1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
                    2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
                    3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
                    4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Atlas V 541 | Perseverance,StatusActive,"145.0 ",Success
                    """),
                new StringReader("""
                    "",Name,Wiki,Rocket Height
                    0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
                    1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
                    2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
                    3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
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
}
