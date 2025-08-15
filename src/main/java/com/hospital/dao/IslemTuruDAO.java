package com.hospital.dao;

import com.hospital.model.IslemTuru;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IslemTuruDAO {

    public List<IslemTuru> findAll() {
        List<IslemTuru> islemler = new ArrayList<>();
        String sql = "SELECT id, islem_adi, kategori, ortalama_sure, aktif " +
                "FROM islem_kategorileri WHERE aktif = TRUE ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                islemler.add(mapResultSetToIslemTuru(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    public IslemTuru findById(int id) {
        String sql = "SELECT id, islem_adi, kategori, ortalama_sure, aktif " +
                "FROM islem_kategorileri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToIslemTuru(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<IslemTuru> findByKategori(String kategori) {
        List<IslemTuru> islemler = new ArrayList<>();
        String sql = "SELECT id, islem_adi, kategori, ortalama_sure, aktif " +
                "FROM islem_kategorileri WHERE aktif = TRUE AND kategori = ? ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kategori);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                islemler.add(mapResultSetToIslemTuru(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    private IslemTuru mapResultSetToIslemTuru(ResultSet rs) throws SQLException {
        IslemTuru islem = new IslemTuru();
        islem.setId(rs.getInt("id"));
        islem.setIslemAdi(rs.getString("islem_adi"));
        islem.setKategori(rs.getString("kategori"));
        islem.setOrtalamaSure(rs.getInt("ortalama_sure"));
        islem.setAktif(rs.getBoolean("aktif"));
        return islem;
    }
}