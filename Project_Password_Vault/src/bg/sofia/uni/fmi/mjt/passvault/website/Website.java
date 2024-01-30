package bg.sofia.uni.fmi.mjt.passvault.website;

public record Website(String url) {

    public static Website of(String str) {
        return new Website(str);
    }
}
