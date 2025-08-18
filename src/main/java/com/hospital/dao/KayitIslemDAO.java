package com.hospital.dao;

import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.KayitIslem;
import com.hospital.model.Islem;
import com.hospital.model.Birim;
import com.hospital.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KayitIslemDAO {

    // Tüm kayıtları getir
    public List<KayitIslem> getAllKayitlar() throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = """
            SELECT k.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            JOIN islemler i ON k.islem_id = i.id
            ORDER BY k.kayit_zamani DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                kayitlar.add(mapResultSetToKayitIslem(rs));
            }
        }
        return kayitlar;
    }

    // Tarih aralığına göre kayıtları getir
    public List<KayitIslem> getKayitlarByDateRange(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = """
            SELECT k.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            JOIN islemler i ON k.islem_id = i.id
            WHERE k.kayit_zamani BETWEEN ? AND ?
            ORDER BY k.kayit_zamani DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(baslangic));
            ps.setTimestamp(2, Timestamp.valueOf(bitis));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kayitlar.add(mapResultSetToKayitIslem(rs));
            }
        }
        return kayitlar;
    }

    // Bugünkü kayıtları getir
    public List<KayitIslem> getBugunkuKayitlar() throws SQLException {
        LocalDateTime bugunBaslangic = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime bugunBitis = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return getKayitlarByDateRange(bugunBaslangic, bugunBitis);
    }

    // Birim bazlı kayıtları getir
    public List<KayitIslem> getKayitlarByBirimId(int birimId, LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = """
            SELECT k.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            JOIN islemler i ON k.islem_id = i.id
            WHERE k.birim_id = ? AND k.kayit_zamani BETWEEN ? AND ?
            ORDER BY k.kayit_zamani DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimId);
            ps.setTimestamp(2, Timestamp.valueOf(baslangic));
            ps.setTimestamp(3, Timestamp.valueOf(bitis));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kayitlar.add(mapResultSetToKayitIslem(rs));
            }
        }
        return kayitlar;
    }

    // Hemşire kategorisi bazlı kayıtları getir
    public List<KayitIslem> getKayitlarByHemsireKategori(String kategori, LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = """
            SELECT k.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            JOIN islemler i ON k.islem_id = i.id
            WHERE k.hemsire_kategori = ? AND k.kayit_zamani BETWEEN ? AND ?
            ORDER BY k.kayit_zamani DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kategori);
            ps.setTimestamp(2, Timestamp.valueOf(baslangic));
            ps.setTimestamp(3, Timestamp.valueOf(bitis));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kayitlar.add(mapResultSetToKayitIslem(rs));
            }
        }
        return kayitlar;
    }

    // Yeni kayıt ekle
    public int insertKayitIslem(KayitIslem kayit) throws SQLException {
        String sql = """
            INSERT INTO kayit_islem (kayit_zamani, hemsire_kategori, islem_id, birim_id, 
                                   gercek_sure, hasta_durumu, notlar) 
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(kayit.getKayitZamani()));
            ps.setString(2, kayit.getHemsireKategori());
            ps.setInt(3, kayit.getIslemId());
            ps.setInt(4, kayit.getBirimId());
            ps.setInt(5, kayit.getGercekSure());
            ps.setString(6, kayit.getHastaDurumu());
            ps.setString(7, kayit.getNotlar());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;
    }

    // Günlük iş yükü özeti getir
    public List<IsYukuOzeti> getGunlukIsYukuOzeti(LocalDateTime tarih) throws SQLException {
        List<IsYukuOzeti> ozetler = new ArrayList<>();
        String sql = """
            SELECT 
                b.birim_adi,
                k.hemsire_kategori,
                COUNT(*) as toplam_islem,
                SUM(k.gercek_sure) as toplam_sure_dk
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            WHERE DATE(k.kayit_zamani) = DATE(?)
            GROUP BY b.birim_adi, k.hemsire_kategori
            ORDER BY b.birim_adi, k.hemsire_kategori
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(tarih));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                IsYukuOzeti ozet = new IsYukuOzeti(
                        rs.getString("birim_adi"),
                        rs.getString("hemsire_kategori"),
                        rs.getInt("toplam_islem"),
                        rs.getInt("toplam_sure_dk")
                );
                ozetler.add(ozet);
            }
        }
        return ozetler;
    }

    // İşlem bazlı ortalama süre analizi
    public double getOrtalamaIslemSuresi(int islemId, String hemsireKategori, int sonGunSayisi) throws SQLException {
        String sql = """
            SELECT AVG(gercek_sure) as ortalama_sure
            FROM kayit_islem
            WHERE islem_id = ? AND hemsire_kategori = ? 
            AND kayit_zamani >= ? 
        """;

        LocalDateTime baslangic = LocalDateTime.now().minusDays(sonGunSayisi);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, islemId);
            ps.setString(2, hemsireKategori);
            ps.setTimestamp(3, Timestamp.valueOf(baslangic));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("ortalama_sure");
            }
        }
        return 0.0;
    }

    // Kritik durumlar analizi
    public List<KayitIslem> getKritikDurumKayitlari(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = """
            SELECT k.*, b.birim_adi, i.islem_adi, i.is_direct 
            FROM kayit_islem k
            JOIN birimler b ON k.birim_id = b.id
            JOIN islemler i ON k.islem_id = i.id
            WHERE k.hasta_durumu = 'kritik' AND k.kayit_zamani BETWEEN ? AND ?
            ORDER BY k.kayit_zamani DESC
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(baslangic));
            ps.setTimestamp(2, Timestamp.valueOf(bitis));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                kayitlar.add(mapResultSetToKayitIslem(rs));
            }
        }
        return kayitlar;
    }

    // ResultSet'i KayitIslem nesnesine çevir
    private KayitIslem mapResultSetToKayitIslem(ResultSet rs) throws SQLException {
        KayitIslem kayit = new KayitIslem();
        kayit.setId(rs.getInt("id"));
        kayit.setKayitZamani(rs.getTimestamp("kayit_zamani").toLocalDateTime());
        kayit.setHemsireKategori(rs.getString("hemsire_kategori"));
        kayit.setIslemId(rs.getInt("islem_id"));
        kayit.setBirimId(rs.getInt("birim_id"));
        kayit.setGercekSure(rs.getInt("gercek_sure"));
        kayit.setHastaDurumu(rs.getString("hasta_durumu"));

        // `hemsire_id` kaldırıldı

        kayit.setNotlar(rs.getString("notlar"));
        kayit.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Navigation properties
        Birim birim = new Birim();
        birim.setId(rs.getInt("birim_id"));
        birim.setBirimAdi(rs.getString("birim_adi"));
        kayit.setBirim(birim);

        Islem islem = new Islem();
        islem.setId(rs.getInt("islem_id"));
        islem.setIslemAdi(rs.getString("islem_adi"));
        islem.setDirect(rs.getBoolean("is_direct"));
        kayit.setIslem(islem);

        return kayit;
    }
}