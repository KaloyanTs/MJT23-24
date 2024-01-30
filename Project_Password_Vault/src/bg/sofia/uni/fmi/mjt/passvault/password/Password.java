package bg.sofia.uni.fmi.mjt.passvault.password;

public class Password {

    public Password() {
        //todo implement
    }

    public String getCiphered(String cipher) {
        //return null;
        //todo implement
        return switch (cipher) {
            case "MD5" -> "1f3870be27";
            case "SHA1" -> "d0be2dc421";
            case "SHA256" -> "3a7bd3e236";
            default -> throw new IllegalStateException("Unexpected value: " + cipher);
        };
    }

    public static Password of(String str) {
        //todo implement
        return null;
    }
}
