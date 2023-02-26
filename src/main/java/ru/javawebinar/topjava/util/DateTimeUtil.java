package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

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

    public static LocalDate parseDate(String string) {
        return parse(LocalDate::parse, string);
    }

    public static LocalTime parseTime(String string) {
        return parse(LocalTime::parse, string);
    }

    private static <T> T parse(Function<String, T> parser, String string) {
        return StringUtils.hasText(string) ? parser.apply(string) : null;
    }
}

