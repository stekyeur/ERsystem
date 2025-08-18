package com.hospital.dao;

import com.hospital.model.Kullanici;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class KullaniciDAO {

    public Kullanici login(String username, String password) {
        String sql = "SELECT kullanici_adi, sifre, adi_soyadi, durum, acilis_tarih FROM kullanici WHERE kullanici_adi = ? AND sifre = ? AND durum = TRUE;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKullanici(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(Kullanici kullanici) {
        String sql = "INSERT INTO kullanici (kullanici_adi, adi_soyadi, sifre, durum) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kullanici.getUsername());
            ps.setString(2, kullanici.getAdSoyad());
            ps.setString(3, kullanici.getPassword()); // Düz metin şifre
            ps.setBoolean(4, true);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Kullanici> findAll() {
        List<Kullanici> kullaniciListesi = new ArrayList<>();
        String sql = "SELECT kullanici_adi, adi_soyadi, sifre, durum, acilis_tarih FROM kullanici WHERE durum = TRUE ORDER BY adi_soyadi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                kullaniciListesi.add(mapResultSetToKullanici(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kullaniciListesi;
    }

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        Kullanici kullanici = new Kullanici();
        kullanici.setUsername(rs.getString("kullanici_adi"));
        kullanici.setAdSoyad(rs.getString("adi_soyadi"));
        kullanici.setPassword(rs.getString("sifre"));
        kullanici.setDurum(rs.getBoolean("durum"));
        kullanici.setAcilisTarihi(rs.getTimestamp("acilis_tarih"));
        return kullanici;
    }
}