package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseColumn implements Column {

    List<String> values;

    public BaseColumn() {
        this(new HashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = new ArrayList<>(values);
    }

    @Override
    public void addData(String data) {
        values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return List.copyOf(values);
    }
}
