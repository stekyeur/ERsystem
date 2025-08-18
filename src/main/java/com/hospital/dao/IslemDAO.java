package com.hospital.dao;

import com.hospital.model.Hemsire;
import com.hospital.model.HemsireTecrube;
import com.hospital.model.Islem;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IslemDAO {

    // Tüm işlemleri getir
    public List<Islem> getAllIslemler() throws SQLException {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT * FROM islemler WHERE true ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                islemler.add(mapResultSetToIslem(rs));
            }
        }
        return islemler;
    }

    // Aktif işlemleri getir
    public List<Islem> getAktifIslemler() throws SQLException {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT * FROM islemler ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                islemler.add(mapResultSetToIslem(rs));
            }
        }
        return islemler;
    }

    // ID'ye göre işlem getir
    public Islem getIslemById(int id) throws SQLException {
        String sql = "SELECT * FROM islemler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToIslem(rs);
            }
        }
        return null;
    }

    // İşlem adına göre işlem getir
    public Islem getIslemByAdi(String islemAdi) throws SQLException {
        String sql = "SELECT * FROM islemler WHERE islem_adi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, islemAdi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToIslem(rs);
            }
        }
        return null;
    }

    // Yeni işlem ekle
    public int insertIslem(Islem islem) throws SQLException {
        String sql = "INSERT INTO islemler (islem_adi, aciklama, is_direct) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, islem.getIslemAdi());
            ps.setString(2, islem.getAciklama());
            ps.setBoolean(3, islem.isDirect());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }

    // İşlem güncelle
    public boolean updateIslem(Islem islem) throws SQLException {
        String sql = "UPDATE islemler SET islem_adi = ?, aciklama = ?, is_direct = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, islem.getIslemAdi());
            ps.setString(2, islem.getAciklama());
            ps.setBoolean(3, islem.isDirect());
            ps.setInt(4, islem.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // İşlem sil
    public boolean deleteIslem(int id) throws SQLException {
        String sql = "DELETE FROM islemler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Direct/Indirect işlemleri getir
    public List<Islem> getIslemlerByType(boolean isDirect) throws SQLException {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT * FROM islemler WHERE is_direct = ? ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isDirect);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                islemler.add(mapResultSetToIslem(rs));
            }
        }
        return islemler;
    }

    // ResultSet'i Islem nesnesine çevir
    private Islem mapResultSetToIslem(ResultSet rs) throws SQLException {
        Islem islem = new Islem();
        islem.setId(rs.getInt("id"));
        islem.setIslemAdi(rs.getString("islem_adi"));
        islem.setAciklama(rs.getString("aciklama"));
        islem.setDirect(rs.getBoolean("is_direct"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            islem.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            islem.setUpdatedAt(updatedTs.toLocalDateTime());
        }

        return islem;
    }
}