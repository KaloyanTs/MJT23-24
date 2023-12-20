package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;

public class Rijndael implements SymmetricBlockCipher {

    SecretKey secretKey;
    private static final int KILOBYTE = 1024;
    private static final String ENCRYPTION_ALGORITHM = "AES"; // //  Advanced Encryption Standard
    private static final int KEY_SIZE_IN_BITS = 128; // Key sizes like 192 or 256 might not be available on all systems

    public Rijndael(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (var cipheredOutput = new CipherOutputStream(outputStream, cipher)) {
                byte[] dataBytes = inputStream.readAllBytes();
                cipheredOutput.write(dataBytes);
                System.out.println("Encryption complete.");
            }
        } catch (Exception e) {
            throw new CipherException("Error with encryption...", e);
        }
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws CipherException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            try (var cipheredOutput = new CipherOutputStream(outputStream, cipher)) {
                byte[] dataBytes = inputStream.readAllBytes();
                cipheredOutput.write(dataBytes);
                System.out.println("Decryption complete.");
            }
        } catch (Exception e) {
            throw new CipherException("Error with decryption...", e);
        }
    }
}
