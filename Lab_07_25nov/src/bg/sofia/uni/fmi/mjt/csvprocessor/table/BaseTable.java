package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

public class BaseTable implements Table {

    List<String> columnHeaders;
    List<Column> columns;

    public BaseTable() {
        this.columnHeaders = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Given data is null...");
        }
        if (!columnHeaders.isEmpty() && data.length != columnHeaders.size()) {
            throw new CsvDataNotCorrectException("Given data length does not match column count...");
        }

        if (columnHeaders.isEmpty()) {
            for (String s : data) {
                columnHeaders.add(s);
                columns.add(new BaseColumn());
            }
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        return List.copyOf(columnHeaders);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("Given bad column...");
        }

        for (int i = 0; i < columnHeaders.size(); ++i) {
            if (column.equals(columnHeaders.get(i))) {
                return List.copyOf(columns.get(i).getData());
            }
        }

        throw new IllegalArgumentException("Given column not found in table...");
    }

    @Override
    public int getRowsCount() {
        return values.size();
    }
}
