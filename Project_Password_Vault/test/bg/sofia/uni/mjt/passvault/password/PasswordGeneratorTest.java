package bg.sofia.uni.mjt.passvault.password;

import bg.sofia.uni.fmi.mjt.passvault.password.PasswordGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordGeneratorTest {

    @Test
    void testGeneratePassword() {
        PasswordGenerator generator = PasswordGenerator.getInstance();

        String password = generator.generatePassword(15).getDecrypted();
        for (char c : password.toCharArray()) {
            assertTrue(c > 40 && c < 127);
        }
    }
}
