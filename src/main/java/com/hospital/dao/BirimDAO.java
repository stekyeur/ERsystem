package com.hospital.dao;

import com.hospital.model.Birim;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BirimDAO {

    public List<Birim> findAll() {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT birim_id, birim_adi, aktif FROM birimler WHERE aktif = TRUE ORDER BY birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birimler;
    }

    public List<Birim> findByIslemId(int islemId) {
        List<Birim> birimler = new ArrayList<>();
        // Eğer birim_islemler tablosu yoksa, tüm birimleri döndür
        String sql = "SELECT birim_id, birim_adi, aktif FROM birimler WHERE aktif = TRUE ORDER BY birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birimler;
    }

    private Birim mapResultSetToBirim(ResultSet rs) throws SQLException {
        Birim birim = new Birim();
        birim.setBirimId(rs.getInt("birim_id"));
        birim.setBirimAdi(rs.getString("birim_adi"));
        birim.setAktif(rs.getBoolean("aktif"));
        return birim;
    }
}