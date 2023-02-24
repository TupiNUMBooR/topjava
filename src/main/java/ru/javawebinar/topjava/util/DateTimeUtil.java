package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenInclusiveAndExclusive(T value, T left, T right) {
        return (left == null || value.compareTo(left) >= 0) &&
                (right == null || value.compareTo(right) < 0);
    }

    public static <T extends Comparable<T>> boolean isBetweenInclusiveAndInclusive(T value, T left, T right) {
        return (left == null || value.compareTo(left) >= 0) &&
                (right == null || value.compareTo(right) <= 0);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

