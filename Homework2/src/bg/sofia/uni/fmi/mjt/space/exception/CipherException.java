package bg.sofia.uni.fmi.mjt.space.exception;

public class CipherException extends Exception {

    public CipherException(String msg) {
        super(msg);
    }

    public CipherException(String msg, Throwable t) {
        super(msg, t);
    }
}
