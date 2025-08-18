package com.hospital.dao;

import com.hospital.model.Birim;
import com.hospital.model.BirimIslem;
import com.hospital.model.Islem;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BirimIslemDAO {

    // Tüm birim-işlem ilişkilerini getir
    public List<BirimIslem> getAllBirimIslemler() throws SQLException {
        List<BirimIslem> birimIslemler = new ArrayList<>();
        String sql = """
            SELECT bi.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM birim_islemler bi
            JOIN birimler b ON bi.birim_id = b.id
            JOIN islemler i ON bi.islem_id = i.id
            WHERE bi.aktif = true
            ORDER BY b.birim_adi, i.islem_adi
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                birimIslemler.add(mapResultSetToBirimIslem(rs));
            }
        }
        return birimIslemler;
    }

    // Birim ID'ye göre işlemleri getir
    public List<BirimIslem> getIslemlerByBirimId(int birimId) throws SQLException {
        List<BirimIslem> birimIslemler = new ArrayList<>();
        String sql = """
            SELECT bi.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM birim_islemler bi
            JOIN birimler b ON bi.birim_id = b.id
            JOIN islemler i ON bi.islem_id = i.id
            WHERE bi.birim_id = ? AND bi.aktif = true
            ORDER BY i.islem_adi
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                birimIslemler.add(mapResultSetToBirimIslem(rs));
            }
        }
        return birimIslemler;
    }

    // İşlem ID'ye göre birimleri getir
    public List<BirimIslem> getBirimlerByIslemId(int islemId) throws SQLException {
        List<BirimIslem> birimIslemler = new ArrayList<>();
        String sql = """
            SELECT bi.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM birim_islemler bi
            JOIN birimler b ON bi.birim_id = b.id
            JOIN islemler i ON bi.islem_id = i.id
            WHERE bi.islem_id = ? AND bi.aktif = true
            ORDER BY b.birim_adi
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, islemId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                birimIslemler.add(mapResultSetToBirimIslem(rs));
            }
        }
        return birimIslemler;
    }

    // Spesifik birim-işlem ilişkisi getir
    public BirimIslem getBirimIslem(int birimId, int islemId) throws SQLException {
        String sql = """
            SELECT bi.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM birim_islemler bi
            JOIN birimler b ON bi.birim_id = b.id
            JOIN islemler i ON bi.islem_id = i.id
            WHERE bi.birim_id = ? AND bi.islem_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimId);
            ps.setInt(2, islemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBirimIslem(rs);
            }
        }
        return null;
    }

    // Yeni birim-işlem ilişkisi ekle
    public int insertBirimIslem(BirimIslem birimIslem) throws SQLException {
        String sql = "INSERT INTO birim_islemler (birim_id, islem_id, t1_sure, t2_sure, t3_sure, aktif) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimIslem.getBirimId());
            ps.setInt(2, birimIslem.getIslemId());
            ps.setInt(3, birimIslem.getT1Sure());
            ps.setInt(4, birimIslem.getT2Sure());
            ps.setInt(5, birimIslem.getT3Sure());
            ps.setBoolean(6, birimIslem.isAktif());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }

    // Birim-işlem ilişkisi güncelle
    public boolean updateBirimIslem(BirimIslem birimIslem) throws SQLException {
        String sql = "UPDATE birim_islemler SET t1_sure = ?, t2_sure = ?, t3_sure = ?, aktif = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimIslem.getT1Sure());
            ps.setInt(2, birimIslem.getT2Sure());
            ps.setInt(3, birimIslem.getT3Sure());
            ps.setBoolean(4, birimIslem.isAktif());
            ps.setInt(5, birimIslem.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Birim-işlem ilişkisi sil
    public boolean deleteBirimIslem(int id) throws SQLException {
        String sql = "DELETE FROM birim_islemler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ResultSet'i BirimIslem nesnesine çevir
    private BirimIslem mapResultSetToBirimIslem(ResultSet rs) throws SQLException {
        BirimIslem birimIslem = new BirimIslem();
        birimIslem.setId(rs.getInt("id"));
        birimIslem.setBirimId(rs.getInt("birim_id"));
        birimIslem.setIslemId(rs.getInt("islem_id"));
        birimIslem.setT1Sure(rs.getInt("t1_sure"));
        birimIslem.setT2Sure(rs.getInt("t2_sure"));
        birimIslem.setT3Sure(rs.getInt("t3_sure"));
        birimIslem.setAktif(rs.getBoolean("aktif"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            birimIslem.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            birimIslem.setUpdatedAt(updatedTs.toLocalDateTime());
        }

        // Navigation properties
        Birim birim = new Birim();
        birim.setId(rs.getInt("birim_id"));
        birim.setBirimAdi(rs.getString("birim_adi"));
        birimIslem.setBirim(birim);

        Islem islem = new Islem();
        islem.setId(rs.getInt("islem_id"));
        islem.setIslemAdi(rs.getString("islem_adi"));
        islem.setDirect(rs.getBoolean("is_direct"));
        birimIslem.setIslem(islem);

        return birimIslem;
    }
}
