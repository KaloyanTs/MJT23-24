package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToVertexFoundException extends GraphAlgorithmException {

    public NoPathToVertexFoundException(String msg) {
        super(msg);
    }

    public NoPathToVertexFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
