package bg.sofia.uni.mjt.passvault.user;

import bg.sofia.uni.fmi.mjt.passvault.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserTest {

    @Test
    void testCompareTo() {
        User a = new User("abc");
        User b = new User("abc");
        User c = new User("abcd");
        assertEquals(0, a.compareTo(b));
        assertNotEquals(0, a.compareTo(c));
    }
}
