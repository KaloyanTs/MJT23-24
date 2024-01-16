package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import bg.sofia.uni.fmi.mjt.cookingcompass.exception.BadCredentialsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EdamamClientTest {

    @Test
    void testEdamamClient() {
        EdamamClient client = new EdamamClient("id", "key");
        assertTrue(client.doesHaveCredentials());
        assertEquals("id", client.getAppId());
        assertEquals("key", client.getAppKey());
    }

    @Test
    void testEdamamClientFails() {
        EdamamClient client = new EdamamClient("id", "key");
        assertThrows(BadCredentialsException.class, () -> client.setCredentials("bad", "bad", "bad"));
    }
}
