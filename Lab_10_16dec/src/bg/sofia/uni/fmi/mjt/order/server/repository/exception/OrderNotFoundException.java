package bg.sofia.uni.fmi.mjt.order.server.repository.exception;

public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String msg) {
        super(msg);
    }

    public OrderNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
