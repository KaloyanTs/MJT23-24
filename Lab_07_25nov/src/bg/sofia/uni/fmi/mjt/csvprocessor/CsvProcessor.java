package bg.sofia.uni.fmi.mjt.csvprocessor;

public class CsvProcessor {
    public CsvProcessor() {
        this(new Table());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }
}
