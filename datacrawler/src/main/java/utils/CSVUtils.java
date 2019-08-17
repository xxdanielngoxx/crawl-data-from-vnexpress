package utils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLine(
            Writer writer,
            List<String> values) throws IOException {
        writeLine(writer, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(
            Writer writer,
            List<String> values,
            char separators
    ) throws IOException{
        writeLine(writer, values, separators, ' ');
    }

    public static void writeLine(
            Writer writer,
            List<String> values,
            char separators,
            char customQuote) throws IOException {

        boolean first = true;

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String value: values) {
            if (!first) {
                stringBuilder.append(separators);
            }
            if (customQuote == ' ') {
                stringBuilder.append(value);
            } else {
                stringBuilder.append(customQuote).append(followCSVFormat(value)).append(customQuote);
            }

            first = false;
        }
        stringBuilder.append("\n");
        writer.append(stringBuilder.toString());
    }

    private static String followCSVFormat(String value){
        String result = value;
        if (result.contains("\"")) {
            result = result.replaceAll("\"","\"\"");
        }
        return result;
    }
}
