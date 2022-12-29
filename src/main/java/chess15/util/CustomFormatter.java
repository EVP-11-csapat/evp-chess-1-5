package chess15.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * CustomFormatter is used to remove constant time date logs from the .log files
 */
public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record.getLevel().toString() + ": " +
                record.getMessage() +
                "\n";
    }
}
