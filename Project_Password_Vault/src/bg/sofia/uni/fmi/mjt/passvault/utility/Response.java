package bg.sofia.uni.fmi.mjt.passvault.utility;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;

import java.io.Serializable;

public record Response(String content, User user, Password password) implements Serializable {
    private static final long serialVersionUID = 25L;

    public Response enableRawString() {
        if (password == null) {
            return this;
        } else {
            return new Response(this.content, this.user, Password.of(password.getDecrypted(), true));
        }
    }
}
