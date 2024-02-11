package bg.sofia.uni.fmi.mjt.passvault.password;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password implements Serializable {

    private static final long serialVersionUID = 666L;
    private static final int HEX_NUM = 0xff;
    private static final int BITS = 256;
    private static final String ALGORITHM = "AES";
    private static final SecretKey SECRET_KEY;
    private byte[] encryptedContent;
    private final String rawString;

    static {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(BITS);
            SECRET_KEY = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unexpected problem occurred...", e);
        } catch (Exception e) {
            throw new IllegalStateException("Something unexpected happened while building ciphers...", e);
        }
    }

    private Password(String str, boolean isRawString) {
        if (isRawString) {
            this.rawString = str;
        } else {
            encryptedContent = encryptArray(str);
            rawString = null;
        }
    }

    public String getDecrypted() {
        return decryptToString(encryptedContent);
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

    private byte[] encryptArray(String str) {
        ByteArrayInputStream inputStreamToEncrypt = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream outputStreamEncrypted = new ByteArrayOutputStream();
        encrypt(inputStreamToEncrypt, outputStreamEncrypted);
        return outputStreamEncrypted.toByteArray();
    }

    private String decryptToString(byte[] str) {
        ByteArrayOutputStream outputStreamDecrypted = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamEncrypted = new ByteArrayInputStream(str);
        decrypt(inputStreamEncrypted, outputStreamDecrypted);
        return outputStreamDecrypted.toString();
    }

    private void encrypt(InputStream inputStream, OutputStream outputStream) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

            try (var cipheredOutput = new CipherOutputStream(outputStream, cipher)) {
                byte[] dataBytes = inputStream.readAllBytes();
                cipheredOutput.write(dataBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error with encryption...", e);
        }
    }

    private void decrypt(InputStream inputStream, OutputStream outputStream) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            try (var cipheredOutput = new CipherOutputStream(outputStream, cipher)) {
                byte[] dataBytes = inputStream.readAllBytes();
                cipheredOutput.write(dataBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error with decryption...", e);
        }
    }

    public static Password of(String str, boolean rawString) {
        return new Password(str, rawString);
    }

    public static Password of(String str) {
        return new Password(str, false);
    }

    public static boolean areEqual(Password p1, Password p2) {
        return p1.getDecrypted().equals(p2.getDecrypted());
    }

    public byte[] getEncrypted() {
        return encryptedContent;
    }

    public String getRawString() {
        if (rawString != null) {
            return rawString;
        } else {
            throw new IllegalStateException("Cannot invoke getRawString when not created with this option...");
        }
    }
}
