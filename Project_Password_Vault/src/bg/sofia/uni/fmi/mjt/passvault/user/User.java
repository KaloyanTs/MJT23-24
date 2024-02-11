package bg.sofia.uni.fmi.mjt.passvault.user;

import java.io.Serializable;

public record User(String name) implements Comparable<User>, Serializable {

    @Override
    public int compareTo(User u) {
        return name.compareTo(u.name);
    }
}
