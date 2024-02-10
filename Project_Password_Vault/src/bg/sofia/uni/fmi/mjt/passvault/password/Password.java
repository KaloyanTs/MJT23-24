package bg.sofia.uni.fmi.mjt.passvault.password;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Password implements Serializable {

    private static final long serialVersionUID = 666L;
    private static final int HEX_NUM = 0xff;
    private static final int BITS = 256;
    private static final String ALGORITHM = "AES";
    private static final Cipher ENCRYPT_CIPHER;
    private static final Cipher DECRYPT_CIPHER;
    private static final SecretKey SECRET_KEY;

    private final String passwordEncrypted;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(BITS);
            SECRET_KEY = keyGenerator.generateKey();
            DECRYPT_CIPHER = Cipher.getInstance(ALGORITHM);
            DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            ENCRYPT_CIPHER = Cipher.getInstance(ALGORITHM);
            ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected problem occurred...", e);
        } catch (Exception e) {
            throw new IllegalStateException("Something unexpected happened while building ciphers...", e);
        }
    }

    private Password(String password) {
        passwordEncrypted = encrypt(password);
    }

    public String getDecrypted() {
        return decrypt(passwordEncrypted);
    }

    public String getCiphered(String cipher) {
        try {
            MessageDigest digest = MessageDigest.getInstance(cipher);
            byte[] hash = digest.digest(getDecrypted().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(HEX_NUM & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithm given for hashing is not recognized...", e);
        }
    }

    private static String encrypt(String input) {
        try {
            byte[] encryptedBytes = ENCRYPT_CIPHER.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Something unexpected occurred while encrypting...", e);
        }
    }

    private static String decrypt(String encryptedString) {
        try {
            byte[] decryptedBytes = DECRYPT_CIPHER.doFinal(Base64.getDecoder().decode(encryptedString));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Something unexpected occurred while decrypting...", e);
        }
    }

    public static Password of(String str) {
        return new Password(str);
    }

    public static boolean areEqual(Password p1, Password p2) {
        return p1.passwordEncrypted.equals(p2.passwordEncrypted);
    }
}
