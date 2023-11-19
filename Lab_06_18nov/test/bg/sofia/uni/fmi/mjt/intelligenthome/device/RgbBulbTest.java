package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RgbBulbTest {

    @Test
    void testRgbBulbTestDifferentId() {
        RgbBulb a1 = new RgbBulb("Vladi", 34, LocalDateTime.now());
        RgbBulb a2 = new RgbBulb("Krastev", 4, LocalDateTime.now());
        RgbBulb a3 = new RgbBulb("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertFalse(a1.getId().equals(a2.getId()));
        Assertions.assertFalse(a2.getId().equals(a3.getId()));
        Assertions.assertFalse(a1.getId().equals(a3.getId()));
    }

    @Test
    void testGetTypeReturnsCorrectType() {
        RgbBulb a1 = new RgbBulb("Vladi", 34, LocalDateTime.now());
        RgbBulb a2 = new RgbBulb("Krastev", 4, LocalDateTime.now());
        RgbBulb a3 = new RgbBulb("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertEquals(a1.getType(), DeviceType.BULB);
        Assertions.assertEquals(a2.getType(), DeviceType.BULB);
        Assertions.assertEquals(a3.getType(), DeviceType.BULB);
    }
}
