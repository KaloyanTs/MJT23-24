package bg.sofia.uni.fmi.mjt.passvault.user;

public record User(String name) {
    //todo consider containing its password

    public static User of(String str) {
        return new User(str);
    }
}
