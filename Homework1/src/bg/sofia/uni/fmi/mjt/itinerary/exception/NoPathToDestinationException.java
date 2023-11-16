package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends Exception {

    public NoPathToDestinationException(String msg) {
        super(msg);
    }

    public NoPathToDestinationException(String msg, Exception e) {
        super(msg, e);
    }
}
