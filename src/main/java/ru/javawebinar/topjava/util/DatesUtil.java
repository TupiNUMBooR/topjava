package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatesUtil {
    private static final DateTimeFormatter HUMAN_READABLE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter HTML_INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static String formatHumanReadable(LocalDateTime dt) {
        return dt.format(HUMAN_READABLE_FORMATTER);
    }

    public static String formatHtmlInput(LocalDateTime dt) {
        return dt.format(HTML_INPUT_FORMATTER);
    }

    public static String format(LocalDateTime dt, String pattern) {
        return dt.format(DateTimeFormatter.ofPattern(pattern));
    }
}
