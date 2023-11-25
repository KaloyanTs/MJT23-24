package bg.sofia.uni.fmi.mjt.csvprocessor.exceptions;

public class CsvDataNotCorrectException extends Exception {

    public CsvDataNotCorrectException(String msg) {
        super(msg);
    }

    public CsvDataNotCorrectException(String msg, Exception e) {
        super(msg, e);
    }
}
