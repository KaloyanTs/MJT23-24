package bg.sofia.uni.fmi.mjt.passvault.client;

import java.io.Serializable;

public record Request(Cookie cookie, String line) implements Serializable {
}
