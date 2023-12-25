package bg.sofia.uni.fmi.mjt.space.exception;

public class TimeFrameMismatchException extends RuntimeException {

    public TimeFrameMismatchException(String str) {
        super(str);
    }

    public TimeFrameMismatchException(String str, Throwable t) {
        super(str, t);
    }
}
