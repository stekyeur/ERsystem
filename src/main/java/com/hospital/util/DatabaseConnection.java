package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Veritabanı bilgilerini doğrudan buraya yazın
    private static final String URL = "jdbc:postgresql://192.168.1.51:5432/acil_servis_db"; // Kendi URL'nizi girin
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Semafor4.";

    static {
        try {
            // PostgreSQL driver yükle
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver bulunamadı!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}