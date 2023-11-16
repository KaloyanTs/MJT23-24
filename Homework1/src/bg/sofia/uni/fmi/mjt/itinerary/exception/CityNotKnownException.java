package bg.sofia.uni.fmi.mjt.itinerary.exception;


public class CityNotKnownException extends Exception {

    public CityNotKnownException(String msg) {
        super(msg);
    }

    public CityNotKnownException(String msg, Exception e) {
        super(msg, e);
    }
}
