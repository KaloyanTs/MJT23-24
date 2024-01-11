package bg.sofia.uni.fmi.mjt.cookingcompass.exception;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String msg) {
        super(msg);
    }

    public NotAuthorizedException(String msg, Throwable t) {
        super(msg, t);
    }
}
