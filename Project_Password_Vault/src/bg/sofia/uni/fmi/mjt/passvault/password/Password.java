package bg.sofia.uni.fmi.mjt.passvault.password;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {


    String passwordEncrypted;

    private Password(String password) {
        //todo implement encrypting with AES
        passwordEncrypted = password;
    }

    private String getDecrypted() {
        //todo implement decrypting with AES
        return passwordEncrypted;
    }

    public String getCiphered(String cipher) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest digest = MessageDigest.getInstance(cipher);
            byte[] hash = digest.digest(getDecrypted().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
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

    public static Password of(String str) {
        return new Password(str);
    }
}
