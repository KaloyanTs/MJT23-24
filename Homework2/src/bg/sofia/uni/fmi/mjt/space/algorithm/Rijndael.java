package bg.sofia.uni.fmi.mjt.space.algorithm;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.io.OutputStream;

public class Rijndael implements SymmetricBlockCipher {

    private final SecretKey secretKey;
    private static final String ENCRYPTION_ALGORITHM = "AES";

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
            }
        } catch (Exception e) {
            throw new CipherException("Error with decryption...", e);
        }
    }
}
