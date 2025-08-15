package com.hospital.dao;

import com.hospital.model.Kullanici;
import com.hospital.util.DatabaseConnection;

import java.sql.*;

public class KullaniciDAO {

    public Kullanici login(String kullaniciAdi, String sifre) {
        String sql = "SELECT kullanici_id, kullanici_adi, sifre, rol FROM kullanicilar " +
                "WHERE kullanici_adi = ? AND sifre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kullaniciAdi);
            ps.setString(2, sifre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKullanici(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Kullanici findById(int id) {
        String sql = "SELECT kullanici_id, kullanici_adi, sifre, rol FROM kullanicilar WHERE kullanici_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKullanici(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        Kullanici kullanici = new Kullanici();
        kullanici.setKullaniciId(rs.getInt("kullanici_id"));
        kullanici.setKullaniciAdi(rs.getString("kullanici_adi"));
        kullanici.setSifre(rs.getString("sifre"));
        kullanici.setRol(rs.getString("rol"));
        return kullanici;
    }
}