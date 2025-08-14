package com.hospital.dao;

import com.hospital.model.Rapor;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaporDAO {

    public boolean create(Rapor rapor) {
        String sql = "INSERT INTO acil_raporlar (rapor_adi, rapor_tipi, format, baslangic_tarihi, " +
                "bitis_tarihi, olusturan_kullanici_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, rapor.getRaporAdi());
            ps.setString(2, rapor.getRaporTipi());
            ps.setString(3, rapor.getFormat());
            ps.setDate(4, (Date) rapor.getBaslangicTarihi());
            ps.setDate(5, (Date) rapor.getBitisTarihi());
            ps.setInt(6, rapor.getOlusturanKullaniciId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rapor.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDurum(int raporId, String durum, String dosyaYolu) {
        String sql = "UPDATE acil_raporlar SET durum = ?, dosya_yolu = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, durum);
            ps.setString(2, dosyaYolu);
            ps.setInt(3, raporId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Rapor> findByKullanici(int kullaniciId) {
        List<Rapor> raporlar = new ArrayList<>();
        String sql = "SELECT r.*, k.ad_soyad as olusturan_adi " +
                "FROM acil_raporlar r " +
                "LEFT JOIN kullanici k ON r.olusturan_kullanici_id = k.id " +
                "WHERE r.olusturan_kullanici_id = ? " +
                "ORDER BY r.olusturma_tarihi DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, kullaniciId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                raporlar.add(mapResultSetToRapor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return raporlar;
    }

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
        return rapor;
    }
}
