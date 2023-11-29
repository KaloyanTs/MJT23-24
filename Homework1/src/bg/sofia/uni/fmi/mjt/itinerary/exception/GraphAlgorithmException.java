package bg.sofia.uni.fmi.mjt.itinerary.exception;

public abstract class GraphAlgorithmException extends Exception {

    public GraphAlgorithmException(String msg) {
        super(msg);
    }

    public GraphAlgorithmException(String msg, Exception e) {
        super(msg, e);
    }
}
