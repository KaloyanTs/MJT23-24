package bg.sofia.uni.fmi.mjt.passvault.server;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;

import java.util.Optional;

public record Response(String content, Optional<Password> password) {

    public static Response combine(Response r1, Response r2) {
        if (r1.password().isPresent()) {
            throw new IllegalArgumentException("First Response must be passwordless...");
        }
        return new Response(r1.content() + System.lineSeparator() + r2.content(), r2.password());
    }
}
