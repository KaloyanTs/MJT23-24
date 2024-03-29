package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseTable implements Table {

    private List<String> columnHeaders;
    private List<Column> columns;
    private int rowCount;

    public BaseTable() {
        this.columnHeaders = new ArrayList<>();
        this.columns = new ArrayList<>();
        rowCount = 0;
    }

    private <T> boolean areUnique(T[] arr) {
        Set<T> s = new HashSet<>();
        Collections.addAll(s, arr);
        return s.size() == arr.length;
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Given data is null...");
        }
        if (!columnHeaders.isEmpty() && data.length != columnHeaders.size()) {
            throw new CsvDataNotCorrectException("Given data length does not match column count...");
        }

        if (rowCount == 0) {
            if (!areUnique(data)) {
                throw new CsvDataNotCorrectException("Column names are not unique...");
            }
            for (String s : data) {
                columnHeaders.add(s);
                columns.add(new BaseColumn());
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                columns.get(i).addData(data[i]);
            }
        }
        ++rowCount;
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
        return rowCount;
    }
}
