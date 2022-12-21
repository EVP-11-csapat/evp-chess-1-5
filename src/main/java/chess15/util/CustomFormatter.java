package chess15.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuffer sb = new StringBuffer();
        sb.append(record.getLevel().toString() + ": ");
        sb.append(record.getMessage());
        sb.append("\n");
        return sb.toString();
    }
}
