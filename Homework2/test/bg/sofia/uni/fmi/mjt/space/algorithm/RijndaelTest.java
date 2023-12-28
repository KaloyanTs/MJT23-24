package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RijndaelTest {

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    private static SecretKey getSecretKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Problem in key generation...", e);
        }
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    @Test
    void testEncryptionDecryption() throws CipherException {

        SecretKey secretKey = getSecretKey();
        Rijndael rijndael = new Rijndael(secretKey);

        String str = "Rijndael algorithm";
        ByteArrayInputStream inputStreamToEncrypt = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream outputStreamEncrypted = new ByteArrayOutputStream();
        rijndael.encrypt(inputStreamToEncrypt, outputStreamEncrypted);
        byte[] encryptedMessage = outputStreamEncrypted.toByteArray();
        ByteArrayOutputStream outputStreamDecrypted = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamEncrypted = new ByteArrayInputStream(encryptedMessage);
        rijndael.decrypt(inputStreamEncrypted, outputStreamDecrypted);

        assertEquals(str, outputStreamDecrypted.toString());
    }
}
