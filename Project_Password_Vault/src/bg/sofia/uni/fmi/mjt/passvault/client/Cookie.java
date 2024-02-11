package bg.sofia.uni.fmi.mjt.passvault.client;

import bg.sofia.uni.fmi.mjt.passvault.user.User;

import java.io.Serializable;

public record Cookie(User user) implements Serializable {
}
