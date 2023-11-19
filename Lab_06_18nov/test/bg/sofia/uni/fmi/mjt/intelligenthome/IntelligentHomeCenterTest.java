package bg.sofia.uni.fmi.mjt.intelligenthome;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.IntelligentHomeCenter;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.MapDeviceStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IntelligentHomeCenterTest {

    IntelligentHomeCenter homeCenter;

    private List<IoTDevice> makeList() {
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1)));
        list.add(new RgbBulb("Krastev", 4, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("df", 4, LocalDateTime.now().minusDays(4)));
        list.add(new WiFiThermostat("Krdfastev", 4, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("dfsa", 54, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("sadaqw", 4, LocalDateTime.now().minusDays(4)));
        list.add(new WiFiThermostat("nnytr", 4908, LocalDateTime.now().minusDays(4)));

        return list;
    }

    @BeforeEach
    void initializeHomeCenter() {
        homeCenter = new IntelligentHomeCenter(new MapDeviceStorage());
    }

    @Test
    void testRegisterGivenNullThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.register(null));
    }

    @Test
    void testRegisterGivenRegisteredDeviceThrows() throws DeviceAlreadyRegisteredException {
        IoTDevice vladi = new AmazonAlexa("Vladi", 46, LocalDateTime.now());
        homeCenter.register(vladi);
        Assertions.assertThrows(DeviceAlreadyRegisteredException.class, () -> homeCenter.register(vladi));
    }

    @Test
    void testRegisterMultipleDevices() throws DeviceAlreadyRegisteredException {
        List<IoTDevice> list = makeList();
        for (IoTDevice d : list) {
            homeCenter.register(d);
        }

        int count = 0;
        for (DeviceType d : DeviceType.values()) {
            count += homeCenter.getDeviceQuantityPerType(d);
        }

        Assertions.assertEquals(count, list.size());
    }

    @Test
    void testUnregisterGivenNullThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.unregister(null));
    }

    @Test
    void testUnregisterNotRegisteredThrows() throws DeviceAlreadyRegisteredException {
        IoTDevice vladi = new AmazonAlexa("Vladi", 46, LocalDateTime.now());
        IoTDevice ivan = new WiFiThermostat("Vanko2", 6, LocalDateTime.now());

        homeCenter.register(vladi);

        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(ivan));
        Assertions.assertDoesNotThrow(() -> homeCenter.unregister(vladi));
        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(ivan));
    }

    @Test
    void testUnregisterWorking() throws DeviceAlreadyRegisteredException {
        IoTDevice vladi = new AmazonAlexa("Vladi", 46, LocalDateTime.now());
        IoTDevice ivan = new WiFiThermostat("Vanko2", 6, LocalDateTime.now());

        homeCenter.register(vladi);
        homeCenter.register(ivan);

        Assertions.assertDoesNotThrow(() -> homeCenter.unregister(vladi));
        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(vladi));
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionCountAndSorted() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);

        List<String> top2 = new ArrayList<>(homeCenter.getTopNDevicesByPowerConsumption(2));

        Assertions.assertEquals(top2.size(), 2);
        Assertions.assertEquals(top2.get(0), a1.getId());
        Assertions.assertEquals(top2.get(1), a3.getId());
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionOversizeQuery() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);

        List<String> top2 = new ArrayList<>(homeCenter.getTopNDevicesByPowerConsumption(10));

        Assertions.assertEquals(top2.size(), 3);
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNegativeCount() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-10));
    }

    @Test
    void testgetFirstNDevicesByRegistrationCountAndSorted() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));
        IoTDevice a4 = new RgbBulb("PoKriloto", 23, LocalDateTime.now().minusHours(2));
        IoTDevice a5 = new AmazonAlexa("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);
        homeCenter.register(a4);
        homeCenter.register(a5);

        List<IoTDevice> top2 = new ArrayList<>(homeCenter.getFirstNDevicesByRegistration(2));

        Assertions.assertEquals(top2.size(), 2);
        //Assertions.assertEquals(top2.get(0), a1);
        //Assertions.assertEquals(top2.get(1), a2);
    }

    @Test
    void testgetFirstNDevicesByRegistrationOversizedQuery() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);

        List<IoTDevice> top2 = new ArrayList<>(homeCenter.getFirstNDevicesByRegistration(40));

        Assertions.assertEquals(top2.size(), 3);
    }

    @Test
    void testgetFirstNDevicesByRegistrationNegativeCount() throws DeviceAlreadyRegisteredException {
        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusSeconds(1));
        IoTDevice a3 = new WiFiThermostat("PoKriloto", 23, LocalDateTime.now().minusHours(2));

        homeCenter.register(a1);
        homeCenter.register(a2);
        homeCenter.register(a3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-40));
    }

    @Test
    void testGetDeviceByIdNullOrNotFoundThrows()
        throws DeviceAlreadyRegisteredException, DeviceNotFoundException {

        IoTDevice a1 = new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1));
        IoTDevice a2 = new RgbBulb("Krastev", 4, LocalDateTime.now().minusDays(4));

        homeCenter.register(a1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(""));
        Assertions.assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById(a2.getId()));
        Assertions.assertDoesNotThrow(() -> homeCenter.getDeviceById(a1.getId()));
        Assertions.assertEquals(homeCenter.getDeviceById(a1.getId()), a1);

    }

    @Test
    void testGetDeviceQuantityPerTypeNullThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceQuantityPerType(null));
    }

    @Test
    void testGetDeviceQuantityPerTypeEachType() throws DeviceAlreadyRegisteredException {
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("Vladi", 34, LocalDateTime.now().minusDays(1)));
        list.add(new RgbBulb("Krastev", 4, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("df", 4, LocalDateTime.now().minusDays(4)));
        list.add(new WiFiThermostat("Krdfastev", 4, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("dfsa", 54, LocalDateTime.now().minusDays(4)));
        list.add(new RgbBulb("sadaqw", 4, LocalDateTime.now().minusDays(4)));
        list.add(new WiFiThermostat("nnytr", 4908, LocalDateTime.now().minusDays(4)));


        for (IoTDevice d : list) {
            homeCenter.register(d);
        }

        Assertions.assertEquals(homeCenter.getDeviceQuantityPerType(DeviceType.BULB), 4);
        Assertions.assertEquals(homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER), 1);
        Assertions.assertEquals(homeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT), 2);
    }

}
