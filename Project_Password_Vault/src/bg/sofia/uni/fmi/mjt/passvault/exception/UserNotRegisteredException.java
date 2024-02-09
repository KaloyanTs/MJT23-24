package bg.sofia.uni.fmi.mjt.passvault.exception;

public class UserNotRegisteredException extends Exception {

    public UserNotRegisteredException(String msg) {
        super(msg);
    }

    public UserNotRegisteredException(String msg, Throwable t) {
        super(msg, t);
    }
}
