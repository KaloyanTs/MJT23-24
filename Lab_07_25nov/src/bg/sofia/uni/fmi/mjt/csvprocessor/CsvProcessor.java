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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CsvProcessor implements CsvProcessorAPI {

    Table table;

//    public CsvProcessor() {
//        this(new Table());
//    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {

    }

//    private int maxLength(Collection<String> list) {
//        int max = 0;
//        for (String s : list) {
//            max = Math.max(max, s.length());
//        }
//        return max;
//    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        TablePrinter printer = new MarkdownTablePrinter();

        Collection<String> converted = printer.printTable(table, alignments);

        try {
            for (String row : converted) {
                writer.write(row);
            }
            writer.flush();
        } catch (IOException e) {
            //todo decide what to do after understanding writer
        }
    }
}
