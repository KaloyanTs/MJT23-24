package bg.sofia.uni.fmi.mjt.passvault.utility;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;

import java.io.Serializable;

public record Response(String content, Password password) implements Serializable {
    private static final long serialVersionUID = 25L;

    public static Response combine(Response r1, Response r2) {
        if (r1.password() != null) {
            throw new IllegalArgumentException("First Response must be passwordless...");
        }
        return new Response(r1.content() + System.lineSeparator() + r2.content(), r2.password());
    }
}
