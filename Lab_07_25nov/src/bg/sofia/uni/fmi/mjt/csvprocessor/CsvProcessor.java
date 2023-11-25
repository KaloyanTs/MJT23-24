package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

public class CsvProcessor implements CsvProcessorAPI {

    static final int MAX_SIZE = 4096;
    Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        String line = null;
        String[] row;

        try {
            char[] buf = new char[MAX_SIZE];
            reader.read(buf);
            String data = String.valueOf(buf).trim();
            String[] dataLines = data.split("\\Q" + System.lineSeparator() + "\\E");
            for (String dataLine : dataLines) {
                String[] t = dataLine.split("\\Q" + delimiter + "\\E");
                //String[] t = dataLine.split(delimiter +"(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                table.addData(t);
            }

        } catch (IOException e) {
            throw new IllegalStateException("Something unexpected happened while reading...");
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        TablePrinter printer = new MarkdownTablePrinter();

        Collection<String> converted = printer.printTable(table, alignments);

        try {
            boolean first = true;
            for (String row : converted) {
                if (first) {
                    first = false;
                } else {
                    writer.write(System.lineSeparator());
                }
                writer.write(row);
                writer.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Something unexpected happened while writing...", e);
        }
    }
}
