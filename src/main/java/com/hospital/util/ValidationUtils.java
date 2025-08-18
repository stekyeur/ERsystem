package com.hospital.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Hemşire kategorisi doğrulaması
    public static boolean isValidHemsireKategori(String kategori) {
        return kategori != null &&
                (kategori.equals("t1") || kategori.equals("t2") || kategori.equals("t3"));
    }

    // Hasta durumu doğrulaması
    public static boolean isValidHastaDurumu(String durum) {
        return durum != null &&
                (durum.equalsIgnoreCase("kritik") || durum.equalsIgnoreCase("normal"));
    }

    // Pozitif sayı kontrolü
    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }

    // String boş mu kontrolü
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    // Email format kontrolü (hemşire bilgileri için)
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
