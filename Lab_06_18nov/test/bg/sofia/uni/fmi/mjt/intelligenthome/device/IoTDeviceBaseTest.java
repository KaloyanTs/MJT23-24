package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IoTDeviceBaseTest {

    @Test
    void testIoTDeviceBaseDifferentId() {
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("Vladi", 56, LocalDateTime.now()));
        list.add(new WiFiThermostat("Krastev", 5, LocalDateTime.now()));
        list.add(new RgbBulb("PoKriloto", 21, LocalDateTime.now()));

        Set<String> ids = new HashSet<>();
        for (IoTDevice d : list) {
            ids.add(d.getId());
        }

        Assertions.assertEquals(ids.size(), list.size());
    }

    @Test
    void testIoTDeviceBaseDifferentUniqueNumberPerDevice() {
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("Vladi", 56, LocalDateTime.now()));
        list.add(new WiFiThermostat("Krastev", 5, LocalDateTime.now()));
        list.add(new RgbBulb("PoKriloto", 21, LocalDateTime.now()));
        list.add(new AmazonAlexa("Alexa", 1, LocalDateTime.now()));

        Set<String> uniqueNumbers = new HashSet<>();
        String[] parts;
        for (IoTDevice d : list) {
            parts = d.getId().split("-");
            Assertions.assertEquals(parts.length, 3);
            uniqueNumbers.add(parts[parts.length - 1]);
        }

        Assertions.assertEquals(uniqueNumbers.size(), list.size());
    }

}
