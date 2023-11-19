package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class WiFiThermostatTest {

    @Test
    void testRgbBulbTestDifferentId() {
        WiFiThermostat a1 = new WiFiThermostat("Vladi", 34, LocalDateTime.now());
        WiFiThermostat a2 = new WiFiThermostat("Krastev", 4, LocalDateTime.now());
        WiFiThermostat a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertFalse(a1.getId().equals(a2.getId()));
        Assertions.assertFalse(a2.getId().equals(a3.getId()));
        Assertions.assertFalse(a1.getId().equals(a3.getId()));
    }

    @Test
    void testGetTypeReturnsCorrectType() {
        WiFiThermostat a1 = new WiFiThermostat("Vladi", 34, LocalDateTime.now());
        WiFiThermostat a2 = new WiFiThermostat("Krastev", 4, LocalDateTime.now());
        WiFiThermostat a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now());

        Assertions.assertEquals(a1.getType(), DeviceType.THERMOSTAT);
        Assertions.assertEquals(a2.getType(), DeviceType.THERMOSTAT);
        Assertions.assertEquals(a3.getType(), DeviceType.THERMOSTAT);
    }
}
