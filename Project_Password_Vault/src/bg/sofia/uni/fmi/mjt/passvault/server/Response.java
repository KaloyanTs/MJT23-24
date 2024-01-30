package bg.sofia.uni.fmi.mjt.passvault.server;

public record Response(String content) {

    public static Response combine(Response r1, Response r2) {
        return new Response(r1.content() + System.lineSeparator() + r2.content());
    }
}
