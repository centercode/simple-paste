package io.github.leaf.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static DateFormat getFormat(String format) {
        return new SimpleDateFormat(format);
    }
}