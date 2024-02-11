package bg.sofia.uni.fmi.mjt.passvault.utility;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;

import java.io.Serializable;

public record Response(String content, User user, Password password) implements Serializable {
    private static final long serialVersionUID = 25L;

    public static Response combine(Response r1, Response r2) {
        if (r1.password() != null || r1.user() != null) {
            throw new IllegalArgumentException("First Response must be userless and passwordless...");
        }
        return new Response(r1.content() + System.lineSeparator() + r2.content(), r2.user(), r2.password());
    }
}
