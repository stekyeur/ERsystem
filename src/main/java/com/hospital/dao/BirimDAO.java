package com.hospital.dao;

import com.hospital.model.Birim;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BirimDAO {

    // Tüm birimleri getir
    public List<Birim> getAllBirimler() throws SQLException {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT * FROM birimler ORDER BY birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        }
        return birimler;
    }

    public List<Birim> getBirimlerByIslemId(int islemId) throws SQLException {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT b.id, b.birim_adi, b.aciklama, b.kapasite, b.aktif, b.created_at, b.updated_at " +
                "FROM birimler b " +
                "JOIN birim_islemler ib ON b.id = ib.birim_id " +
                "WHERE ib.islem_id = ? AND b.aktif = true " +
                "ORDER BY b.birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, islemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    birimler.add(mapResultSetToBirim(rs));
                }
            }
        }
        return birimler;
    }

    // Aktif birimleri getir
    public List<Birim> getAktifBirimler() throws SQLException {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT * FROM birimler WHERE aktif = true ORDER BY birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        }
        return birimler;
    }

    // ID'ye göre birim getir
    public Birim getBirimById(int id) throws SQLException {
        String sql = "SELECT * FROM birimler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBirim(rs);
            }
        }
        return null;
    }

    // Birim adına göre birim getir
    public Birim getBirimByAdi(String birimAdi) throws SQLException {
        String sql = "SELECT * FROM birimler WHERE birim_adi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, birimAdi);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBirim(rs);
            }
        }
        return null;
    }

    // Yeni birim ekle
    public int insertBirim(Birim birim) throws SQLException {
        String sql = "INSERT INTO birimler (birim_adi, aciklama, kapasite, aktif) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, birim.getBirimAdi());
            ps.setString(2, birim.getAciklama());
            ps.setInt(3, birim.getKapasite());
            ps.setBoolean(4, birim.isAktif());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }

    // Birim güncelle
    public boolean updateBirim(Birim birim) throws SQLException {
        String sql = "UPDATE birimler SET birim_adi = ?, aciklama = ?, kapasite = ?, aktif = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, birim.getBirimAdi());
            ps.setString(2, birim.getAciklama());
            ps.setInt(3, birim.getKapasite());
            ps.setBoolean(4, birim.isAktif());
            ps.setInt(5, birim.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Birim sil
    public boolean deleteBirim(int id) throws SQLException {
        String sql = "DELETE FROM birimler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet'i Birim nesnesine çevir
    private Birim mapResultSetToBirim(ResultSet rs) throws SQLException {
        Birim birim = new Birim();
        birim.setId(rs.getInt("id"));
        birim.setBirimAdi(rs.getString("birim_adi"));
        birim.setAciklama(rs.getString("aciklama"));
        birim.setKapasite(rs.getInt("kapasite"));
        birim.setAktif(rs.getBoolean("aktif"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            birim.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            birim.setUpdatedAt(updatedTs.toLocalDateTime());
        }

        return birim;
    }
}