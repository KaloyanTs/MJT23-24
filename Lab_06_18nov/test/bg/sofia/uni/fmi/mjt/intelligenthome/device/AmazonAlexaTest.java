package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class AmazonAlexaTest {

    @Test
    void testAmazonAlexaDifferentId() {
        AmazonAlexa a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now());
        AmazonAlexa a2 = new AmazonAlexa("Krastev", 4, LocalDateTime.now());
        AmazonAlexa a3 = new AmazonAlexa("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertFalse(a1.getId().equals(a2.getId()));
        Assertions.assertFalse(a2.getId().equals(a3.getId()));
        Assertions.assertFalse(a1.getId().equals(a3.getId()));
    }

    @Test
    void testGetTypeReturnsCorrectType() {
        AmazonAlexa a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now());
        AmazonAlexa a2 = new AmazonAlexa("Krastev", 4, LocalDateTime.now());
        AmazonAlexa a3 = new AmazonAlexa("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertEquals(a1.getType(), DeviceType.SMART_SPEAKER);
        Assertions.assertEquals(a2.getType(), DeviceType.SMART_SPEAKER);
        Assertions.assertEquals(a3.getType(), DeviceType.SMART_SPEAKER);
    }
}
