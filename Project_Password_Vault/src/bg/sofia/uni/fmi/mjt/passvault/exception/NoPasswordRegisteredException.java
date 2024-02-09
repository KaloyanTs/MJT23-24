package bg.sofia.uni.fmi.mjt.passvault.exception;

public class NoPasswordRegisteredException extends Exception {

    public NoPasswordRegisteredException(String msg) {
        super(msg);
    }

    public NoPasswordRegisteredException(String msg, Throwable t) {
        super(msg, t);
    }
}
