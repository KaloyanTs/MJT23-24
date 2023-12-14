package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MJTSpaceScannerTest {

    @Test
    void testNothing() {
        fail();
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
