package bg.sofia.uni.mjt.passvault.password;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PasswordTest {

    private static final String passString = "my$trongPa$$word";
    private static Password password;

    @BeforeAll
    static void createPassword() {
        password = Password.of(passString);
    }

    @Test
    void testGetDecrypted() {
        assertEquals(passString, password.getDecrypted(), "Decryption must be reverse of encryption.");
    }

    @Test
    void testGetCipheredSHA256() {
        assertEquals("422b2df86670ca39f66eb89dbea32edbbbf807955362a504992da73c29975ab0",
            password.getCiphered("SHA256"),
            "hashing with SHA256 is not correct");
    }

    @Test
    void testGetCipheredSHA1() {
        assertEquals("6ee39f6518f9667fb8aa8b1dd7b1cada2ac54554",
            password.getCiphered("SHA1"),
            "hashing with SHA1 is not correct");
    }

    @Test
    void testGetCipheredMD5() {
        assertEquals("8c55222e234196647c8717e5003050f5",
            password.getCiphered("MD5"),
            "hashing with MD5 is not correct");
    }

    @Test
    void testAreEqual() {
        Password p1 = Password.of("abcdefg");
        Password p2 = Password.of("abcdefg");
        Password p3 = Password.of("abcdEfg");
        assertEquals(p1, p2, "Password with equal contents must be equal");
        assertNotEquals(p1, p3, "Passwords with different contents must not be equal");
    }
}
