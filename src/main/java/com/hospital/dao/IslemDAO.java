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
        String sql = "INSERT INTO islemler (tarih, hemsire_id, islem_id, birim_id, sure_dakika, islem_tipi, kritiklik, notlar, kaydeden_kullanici_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(islem.getTarih().getTime()));
            stmt.setInt(2, islem.getHemsireId());
            stmt.setInt(3, islem.getIslemId());
            stmt.setInt(4, islem.getBirimId());
            stmt.setInt(5, islem.getSureDakika());
            stmt.setString(6, islem.getIslemTipi());
            stmt.setString(7, islem.getKritiklik());
            stmt.setString(8, islem.getNotlar());
            stmt.setInt(9, islem.getKaydedenKullaniciId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Islem> findByDateRange(Date baslangic, Date bitis) {
        List<Islem> islemler = new ArrayList<>();
        String sql = "SELECT i.*, h.ad_soyad as hemsire_adi, h.tecrube_seviyesi, " +
                "ik.islem_adi, b.birim_adi, k.kullanici_adi as kaydeden_adi " +
                "FROM islemler i " +
                "LEFT JOIN hemsireler h ON i.hemsire_id = h.hemsire_id " +
                "LEFT JOIN islem_kategorileri ik ON i.islem_id = ik.id " +
                "LEFT JOIN birimler b ON i.birim_id = b.birim_id " +
                "LEFT JOIN kullanicilar k ON i.kaydeden_kullanici_id = k.kullanici_id " +
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
        String sql = "SELECT i.*, h.ad_soyad as hemsire_adi, h.tecrube_seviyesi, " +
                "ik.islem_adi, b.birim_adi, k.kullanici_adi as kaydeden_adi " +
                "FROM islemler i " +
                "LEFT JOIN hemsireler h ON i.hemsire_id = h.hemsire_id " +
                "LEFT JOIN islem_kategorileri ik ON i.islem_id = ik.id " +
                "LEFT JOIN birimler b ON i.birim_id = b.birim_id " +
                "LEFT JOIN kullanicilar k ON i.kaydeden_kullanici_id = k.kullanici_id " +
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

    public List<Object[]> getGunlukIstatistikler(Date tarih) {
        List<Object[]> sonuclar = new ArrayList<>();
        String sql = "SELECT h.tecrube_seviyesi, ik.islem_adi, i.islem_tipi, i.kritiklik, " +
                "COUNT(*) as toplam_islem, AVG(i.sure_dakika) as ortalama_sure " +
                "FROM islemler i " +
                "INNER JOIN hemsireler h ON i.hemsire_id = h.hemsire_id " +
                "INNER JOIN islem_kategorileri ik ON i.islem_id = ik.id " +
                "WHERE i.tarih = ? " +
                "GROUP BY h.tecrube_seviyesi, ik.islem_adi, i.islem_tipi, i.kritiklik " +
                "ORDER BY h.tecrube_seviyesi, ik.islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, tarih);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getString("tecrube_seviyesi"),
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

        if (rs.getString("hemsire_adi") != null) {
            Hemsire hemsire = new Hemsire();
            hemsire.setId(islem.getHemsireId());
            hemsire.setAdSoyad(rs.getString("hemsire_adi"));
            hemsire.setTecrubeSeviyesi(rs.getString("tecrube_seviyesi"));

            HemsireTecrube tecrube = new HemsireTecrube();
            tecrube.setKategoriAdi(rs.getString("tecrube_seviyesi"));
            hemsire.setTecrube(tecrube);

            islem.setHemsire(hemsire);
        }

        return islem;
    }
}