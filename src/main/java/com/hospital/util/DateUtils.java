package com.hospital.util;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Bugünün başlangıcı (00:00:00)
    public static LocalDateTime getStartOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    // Bugünün sonu (23:59:59)
    public static LocalDateTime getEndOfToday() {
        return LocalDate.now().atTime(23, 59, 59);
    }

    // N gün öncesinin başlangıcı
    public static LocalDateTime getStartOfDaysAgo(int days) {
        return LocalDate.now().minusDays(days).atStartOfDay();
    }

    // Tarih aralığı kontrolü
    public static boolean isInDateRange(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return date.isAfter(start) && date.isBefore(end);
    }

    // Saat formatında gösterim
    public static String formatDurationAsHours(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format("%d:%02d", hours, mins);
    }
}
