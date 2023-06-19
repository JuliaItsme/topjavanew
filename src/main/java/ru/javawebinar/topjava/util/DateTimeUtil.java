package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime start, LocalTime end) {
        return (start == null || lt.compareTo(start) >= 0) && (end == null || lt.compareTo(end) < 0);
    }

    public static boolean isBetween(LocalDate lt, LocalDate start, LocalDate end) {
        return (start == null || lt.compareTo(start) >= 0) && (end == null || lt.compareTo(end) < 0);
    }

    public static boolean isBetween(LocalDateTime lt, LocalDateTime start, LocalDateTime end) {
        return (start == null || lt.compareTo(start) >= 0) && (end == null || lt.compareTo(end) < 0);
    }

    public static LocalDate parseDate(String str) {
         return StringUtils.hasLength(str) ? LocalDate.parse(str) : null;
    }

    public static LocalTime parseTime(String str) {
         return StringUtils.hasLength(str) ? LocalTime.parse(str) : null;
    }
    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

