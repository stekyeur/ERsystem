package com.hospital.dao;

import com.hospital.model.HemsireTecrube;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HemsireTecrubeDAO {

    public List<HemsireTecrube> findAll() {
        List<HemsireTecrube> kategoriler = new ArrayList<>();
        String sql = "SELECT * FROM acil_hemsire_tecrube WHERE aktif = 1 ORDER BY kategori_kodu";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                kategoriler.add(mapResultSetToHemsireTecrube(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kategoriler;
    }

    public HemsireTecrube findById(int id) {
        String sql = "SELECT * FROM acil_hemsire_tecrube WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToHemsireTecrube(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HemsireTecrube mapResultSetToHemsireTecrube(ResultSet rs) throws SQLException {
        HemsireTecrube tecrube = new HemsireTecrube();
        tecrube.setId(rs.getInt("id"));
        tecrube.setKategoriKodu(rs.getString("kategori_kodu"));
        tecrube.setKategoriAdi(rs.getString("kategori_adi"));
        tecrube.setAciklama(rs.getString("aciklama"));
        tecrube.setAktif(rs.getBoolean("aktif"));
        return tecrube;
    }
}
