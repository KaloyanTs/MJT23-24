package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MarkdownTablePrinter implements TablePrinter {

    private final static String DELIMITER = "|";
    private final static String SPACE = " ";
    private final static String BORDER = "-";
    private final static int MIN_LENGTH = 3;

    private int maxLength(String header, Collection<String> list) {
        int max = header.length();
        for (String s : list) {
            max = Math.max(max, s.length());
        }
        return max;
    }

    private String copies(String s, int n) {
        return String.join("", Collections.nCopies(n, s));
    }

    private void printHeader(Collection<String> columns, Table table, int[] columnLengths, List<List<String>> values,
                             Collection<String> res) {

        int i = 0;
        String row = "";
        Collection<String> colData;
        for (String column : columns) {
            colData = table.getColumnData(column);
            values.add(new ArrayList(colData));
            columnLengths[i] = Math.max(maxLength(column, colData), MIN_LENGTH);
            row += DELIMITER + SPACE + column + copies(SPACE, columnLengths[i++] - column.length()) + SPACE;
        }
        row += DELIMITER;
        res.add(row);
    }

    private void printRows(Table table, int[] columnLengths, List<List<String>> values, Collection<String> res) {
        String row;
        for (int j = 0; j < table.getRowsCount(); j++) {
            row = "";
            for (int k = 0; k < columnLengths.length; k++) {
                row +=
                    DELIMITER + SPACE +
                        values.get(k).get(j) +
                        copies(SPACE, columnLengths[k] - values.get(k).get(j).length()) +
                        SPACE;
            }
            row += DELIMITER;
            res.add(row);
        }
    }

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Collection<String> res = new ArrayList<>();
        Collection<String> columns = table.getColumnNames();
        int[] columnLengths = new int[columns.size()];
        List<List<String>> values = new ArrayList<>();

        printHeader(columns, table, columnLengths, values, res);

        String row = "";
        String borderPrint;
        for (int j = 0; j < columnLengths.length; ++j) {
            if (j < alignments.length) {
                borderPrint = switch (alignments[j]) {
                    case LEFT -> ":" + copies(BORDER, columnLengths[j] - 1);
                    case RIGHT -> copies(BORDER, columnLengths[j] - 1) + ":";
                    case CENTER -> ":" + copies(BORDER, columnLengths[j] - 2) + ":";
                    case NOALIGNMENT -> copies(BORDER, columnLengths[j]);
                };
            } else borderPrint = copies(BORDER, columnLengths[j]);

            row += DELIMITER + SPACE + borderPrint + SPACE;
        }
        row += DELIMITER;
        res.add(row);

        printRows(table, columnLengths, values, res);

        return res;
    }
}
