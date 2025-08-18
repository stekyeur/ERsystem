package com.hospital.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://192.168.1.51:5432/acil_servis_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Semafor4.";

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException ex) {
                throw new SQLException("PostgreSQL JDBC sürücüsü bulunamadı.", ex);
            }
        }
        return connection;
    }
}