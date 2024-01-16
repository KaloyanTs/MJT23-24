package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EdamamClientTest {

    @Test
    void testEdamamClient() {
        EdamamClient client = new EdamamClient("id", "key");
        assertTrue(client.doesHaveCredentials());
        assertEquals("id", client.getAppId());
        assertEquals("key", client.getAppKey());
    }
}
