package com.hospital.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9+\\-\\s\\(\\)]{10,15}$");

    private static final Pattern SICIL_NO_PATTERN =
            Pattern.compile("^[A-Za-z0-9]{3,20}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidSicilNo(String sicilNo) {
        return sicilNo != null && SICIL_NO_PATTERN.matcher(sicilNo).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isPositiveInteger(String str) {
        try {
            int value = Integer.parseInt(str);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDuration(int duration) {
        return duration > 0 && duration <= 1440; // Maksimum 24 saat (1440 dakika)
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        // HTML ve SQL injection saldırılarına karşı basit koruma
        return input.trim()
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("&", "&amp;");
    }
}
