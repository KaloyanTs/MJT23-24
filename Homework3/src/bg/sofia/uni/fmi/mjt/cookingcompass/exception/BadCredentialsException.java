package bg.sofia.uni.fmi.mjt.cookingcompass.exception;

public class BadCredentialsException extends Exception {

    public BadCredentialsException(String msg) {
        super(msg);
    }

    public BadCredentialsException(String msg, Throwable t) {
        super(msg, t);
    }
}
