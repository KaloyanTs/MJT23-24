package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.Collection;

public class BaseColumn implements Column {

    Set<String> values;

    public BaseColumn() {
        this(new HashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }

    @Override
    public void addData(String data) {

    }

    @Override
    public Collection<String> getData() {
        return null;
    }
}
