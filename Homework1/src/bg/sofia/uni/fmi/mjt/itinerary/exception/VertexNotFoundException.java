package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class VertexNotFoundException extends GraphAlgorithmException {
    public VertexNotFoundException(String msg) {
        super(msg);
    }

    public VertexNotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
