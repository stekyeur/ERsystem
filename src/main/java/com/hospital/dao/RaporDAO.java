package com.hospital.dao;

import com.hospital.model.Rapor;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaporDAO {

    private static final String INSERT_SQL =
            "INSERT INTO raporlar (rapor_adi, rapor_tipi, format, baslangic_tarihi, bitis_tarihi, " +
                    "olusturan_kullanici_id, durum, aciklama) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE raporlar SET dosya_yolu = ?, durum = ?, aciklama = ?, dosya_boyutu = ? WHERE id = ?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM raporlar WHERE id = ?";

    private static final String SELECT_BY_KULLANICI_SQL =
            "SELECT * FROM raporlar WHERE olusturan_kullanici_id = ? ORDER BY olusturma_tarihi DESC";

    private static final String DELETE_SQL =
            "DELETE FROM raporlar WHERE id = ?";

    private static final String SELECT_FILTERED_BASE_SQL =
            "SELECT * FROM raporlar WHERE olusturan_kullanici_id = ? ";

    // Rapor ekleme
    public int insert(Rapor rapor) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, rapor.getRaporAdi());
            stmt.setString(2, rapor.getRaporTipi());
            stmt.setString(3, rapor.getFormat());
            stmt.setDate(4, new java.sql.Date(rapor.getBaslangicTarihi().getTime()));
            stmt.setDate(5, new java.sql.Date(rapor.getBitisTarihi().getTime()));
            stmt.setInt(6, rapor.getOlusturanKullaniciId());
            stmt.setString(7, rapor.getDurum());
            stmt.setString(8, rapor.getAciklama());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Rapor eklenirken hata olustu.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Rapor ID'si alinamadi.");
                }
            }
        }
    }

    // Rapor güncelleme
    public boolean update(Rapor rapor) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, rapor.getDosyaYolu());
            stmt.setString(2, rapor.getDurum());
            stmt.setString(3, rapor.getAciklama());
            stmt.setLong(4, rapor.getDosyaBoyutu());
            stmt.setInt(5, rapor.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // ID ile rapor bulma
    public Rapor findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRapor(rs);
                }
            }
        }
        return null;
    }

    // Kullanıcının raporlarını getirme
    public List<Rapor> findByKullaniciId(int kullaniciId) throws SQLException {
        List<Rapor> raporlar = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_KULLANICI_SQL)) {

            stmt.setInt(1, kullaniciId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    raporlar.add(mapResultSetToRapor(rs));
                }
            }
        }

        return raporlar;
    }

    // Filtrelenmiş rapor listesi
    public List<Rapor> findFiltered(int kullaniciId, String durum, String tip, String format)
            throws SQLException {
        List<Rapor> raporlar = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(SELECT_FILTERED_BASE_SQL);

        if (durum != null && !durum.isEmpty()) {
            sqlBuilder.append(" AND durum = ?");
        }
        if (tip != null && !tip.isEmpty()) {
            sqlBuilder.append(" AND rapor_tipi = ?");
        }
        if (format != null && !format.isEmpty()) {
            sqlBuilder.append(" AND format = ?");
        }

        sqlBuilder.append(" ORDER BY olusturma_tarihi DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;
            stmt.setInt(paramIndex++, kullaniciId);

            if (durum != null && !durum.isEmpty()) {
                stmt.setString(paramIndex++, durum);
            }
            if (tip != null && !tip.isEmpty()) {
                stmt.setString(paramIndex++, tip);
            }
            if (format != null && !format.isEmpty()) {
                stmt.setString(paramIndex++, format);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    raporlar.add(mapResultSetToRapor(rs));
                }
            }
        }

        return raporlar;
    }

    // Rapor silme
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // ResultSet'ten Rapor nesnesine dönüştürme
    private Rapor mapResultSetToRapor(ResultSet rs) throws SQLException {
        Rapor rapor = new Rapor();

        rapor.setId(rs.getInt("id"));
        rapor.setRaporAdi(rs.getString("rapor_adi"));
        rapor.setRaporTipi(rs.getString("rapor_tipi"));
        rapor.setFormat(rs.getString("format"));
        rapor.setDosyaYolu(rs.getString("dosya_yolu"));
        rapor.setOlusturmaTarihi(rs.getTimestamp("olusturma_tarihi"));
        rapor.setOlusturanKullaniciId(rs.getInt("olusturan_kullanici_id"));
        rapor.setBaslangicTarihi(rs.getDate("baslangic_tarihi"));
        rapor.setBitisTarihi(rs.getDate("bitis_tarihi"));
        rapor.setDurum(rs.getString("durum"));
        rapor.setAciklama(rs.getString("aciklama"));
        rapor.setDosyaBoyutu(rs.getLong("dosya_boyutu"));

        return rapor;
    }
}
