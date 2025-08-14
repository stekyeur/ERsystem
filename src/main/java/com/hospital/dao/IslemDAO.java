package com.hospital.dao;

import com.hospital.model.Hemsire;
import com.hospital.model.HemsireTecrube;
import com.hospital.model.Islem;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IslemDAO {

    public boolean create(Islem islem) {
        String sql = "INSERT INTO acil_islem (tarih, hemsire_id, islem_id, birim_id, sure_dakika, " +
                "islem_tipi, kritiklik, notlar, kaydeden_kullanici_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, (Date) islem.getTarih());
            ps.setInt(2, islem.getHemsireId());
            ps.setInt(3, islem.getIslemId());
            ps.setInt(4, islem.getBirimId());
            ps.setInt(5, islem.getSureDakika());
            ps.setString(6, islem.getIslemTipi());
            ps.setString(7, islem.getKritiklik());
            ps.setString(8, islem.getNotlar());
            ps.setInt(9, islem.getKaydedenKullaniciId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Islem> findByDateRange(Date baslangic, Date bitis) {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT i.*, h.ad_soyad as hemsire_adi, ht.kategori_kodu, " +
                "it.islem_adi, b.birim_adi, k.ad_soyad as kaydeden_adi " +
                "FROM acil_islem i " +
                "LEFT JOIN acil_hemsire h ON i.hemsire_id = h.id " +
                "LEFT JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "LEFT JOIN acil_islem_turleri it ON i.islem_id = it.id " +
                "LEFT JOIN acil_birimler b ON i.birim_id = b.id " +
                "LEFT JOIN kullanici k ON i.kaydeden_kullanici_id = k.id " +
                "WHERE i.tarih BETWEEN ? AND ? " +
                "ORDER BY i.tarih DESC, i.kayit_tarihi DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, baslangic);
            ps.setDate(2, bitis);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                islemler.add(mapResultSetToIslem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    public List<Islem> findByHemsireAndDate(int hemsireId, Date tarih) {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT i.*, h.ad_soyad as hemsire_adi, ht.kategori_kodu, " +
                "it.islem_adi, b.birim_adi, k.ad_soyad as kaydeden_adi " +
                "FROM acil_islem i " +
                "LEFT JOIN acil_hemsire h ON i.hemsire_id = h.id " +
                "LEFT JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "LEFT JOIN acil_islem_turleri it ON i.islem_id = it.id " +
                "LEFT JOIN acil_birimler b ON i.birim_id = b.id " +
                "LEFT JOIN kullanici k ON i.kaydeden_kullanici_id = k.id " +
                "WHERE i.hemsire_id = ? AND i.tarih = ? " +
                "ORDER BY i.kayit_tarihi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hemsireId);
            ps.setDate(2, tarih);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                islemler.add(mapResultSetToIslem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    // Günlük istatistikler için
    public List<Object[]> getGunlukIstatistikler(Date tarih) {
        List<Object[]> sonuclar = new ArrayList<>();
        String sql = "SELECT ht.kategori_kodu, it.islem_adi, i.islem_tipi, i.kritiklik, " +
                "COUNT(*) as toplam_islem, AVG(i.sure_dakika) as ortalama_sure " +
                "FROM acil_islem i " +
                "INNER JOIN acil_hemsire h ON i.hemsire_id = h.id " +
                "INNER JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "INNER JOIN acil_islem_turleri it ON i.islem_id = it.id " +
                "WHERE i.tarih = ? " +
                "GROUP BY ht.kategori_kodu, it.islem_adi, i.islem_tipi, i.kritiklik " +
                "ORDER BY ht.kategori_kodu, it.islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, tarih);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("kategori_kodu"),
                        rs.getString("islem_adi"),
                        rs.getString("islem_tipi"),
                        rs.getString("kritiklik"),
                        rs.getInt("toplam_islem"),
                        rs.getDouble("ortalama_sure")
                };
                sonuclar.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sonuclar;
    }

    private Islem mapResultSetToIslem(ResultSet rs) throws SQLException {
        Islem islem = new Islem();
        islem.setId(rs.getInt("id"));
        islem.setKayitIndeksi(rs.getInt("kayit_indeksi"));
        islem.setTarih(rs.getDate("tarih"));
        islem.setHemsireId(rs.getInt("hemsire_id"));
        islem.setIslemId(rs.getInt("islem_id"));
        islem.setBirimId(rs.getInt("birim_id"));
        islem.setSureDakika(rs.getInt("sure_dakika"));
        islem.setIslemTipi(rs.getString("islem_tipi"));
        islem.setKritiklik(rs.getString("kritiklik"));
        islem.setNotlar(rs.getString("notlar"));
        islem.setKayitTarihi(rs.getTimestamp("kayit_tarihi"));
        islem.setKaydedenKullaniciId(rs.getInt("kaydeden_kullanici_id"));

        // İlişkili objeler için basit mapping (sadece isimler)
        if (rs.getString("hemsire_adi") != null) {
            Hemsire hemsire = new Hemsire();
            hemsire.setId(islem.getHemsireId());
            hemsire.setAdSoyad(rs.getString("hemsire_adi"));

            if (rs.getString("kategori_kodu") != null) {
                HemsireTecrube tecrube = new HemsireTecrube();
                tecrube.setKategoriKodu(rs.getString("kategori_kodu"));
                hemsire.setTecrube(tecrube);
            }
            islem.setHemsire(hemsire);
        }

        return islem;
    }
}
