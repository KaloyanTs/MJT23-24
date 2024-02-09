package bg.sofia.uni.fmi.mjt.passvault.exception;

public class BadCommandArgumentsException extends Exception {

    public BadCommandArgumentsException(String msg) {
        super(msg);
    }

    public BadCommandArgumentsException(String msg, Throwable t) {
        super(msg, t);
    }
}
