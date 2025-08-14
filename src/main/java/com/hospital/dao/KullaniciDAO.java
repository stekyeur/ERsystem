package com.hospital.dao;

import com.hospital.model.Kullanici;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KullaniciDAO {

    public Kullanici login(String username, String password) {
        String sql = "SELECT * FROM kullanicilar WHERE kullanici_adi = ? AND sifre = ? AND aktif = TRUE;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToKullanici(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(Kullanici kullanici) {
        String sql = "INSERT INTO kullanici (username, password, ad_soyad, email, rol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kullanici.getUsername());
            ps.setString(2, kullanici.getPassword());
            ps.setString(3, kullanici.getAdSoyad());
            ps.setString(4, kullanici.getEmail());
            ps.setString(5, kullanici.getRol());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Kullanici> findAll() {
        List<Kullanici> kullanicilar = new ArrayList<>();
        String sql = "SELECT * FROM kullanici WHERE aktif = 1 ORDER BY ad_soyad";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                kullanicilar.add(mapResultSetToKullanici(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kullanicilar;
    }

    private Kullanici mapResultSetToKullanici(ResultSet rs) throws SQLException {
        Kullanici kullanici = new Kullanici();
        kullanici.setId(rs.getInt("kullanici_id"));
        kullanici.setUsername(rs.getString("kullanici_adi"));
        kullanici.setPassword(rs.getString("sifre"));
        kullanici.setRol(rs.getString("yetki"));
        kullanici.setAktif(rs.getBoolean("aktif"));
        kullanici.setKayitTarihi(rs.getTimestamp("olusturma_tarihi"));
        // email ve adSoyad alanları DB’de yok, eğer modelde varsa null bırak
        kullanici.setAdSoyad(rs.getString("kullanici_adi"));
        kullanici.setEmail(null);
        return kullanici;
    }

}
