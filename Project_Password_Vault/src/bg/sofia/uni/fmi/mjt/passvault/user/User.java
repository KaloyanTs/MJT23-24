package bg.sofia.uni.fmi.mjt.passvault.user;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;

public record User(String name, Password password) {
}
