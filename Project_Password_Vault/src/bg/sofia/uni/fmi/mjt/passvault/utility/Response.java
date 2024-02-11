package bg.sofia.uni.fmi.mjt.passvault.utility;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;
import bg.sofia.uni.fmi.mjt.passvault.user.User;

import java.io.Serializable;

public record Response(String content, User user, Password password) implements Serializable {
    private static final long serialVersionUID = 25L;
}
