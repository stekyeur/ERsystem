package com.hospital.dao;

import com.hospital.model.Hemsire;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HemsireDAO {

    /**
     * Tüm aktif hemşireleri ve tecrübe seviyelerini listeler.
     * @return Aktif hemsire listesi
     * @throws SQLException Veritabanı hatası
     */
    public List<Hemsire> findAll() throws SQLException {
        List<Hemsire> hemsireler = new ArrayList<>();
        String sql = "SELECT hemsire_id, personel_no, ad_soyad, tecrube_seviyesi, kurum_id, aktif, ise_giris_tarihi " +
                "FROM hemsireler WHERE aktif = true ORDER BY ad_soyad";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                hemsireler.add(mapResultSetToHemsire(rs));
            }
        }
        return hemsireler;
    }

    /**
     * Yeni bir hemşire kaydı ekler.
     * @param hemsire Eklenecek Hemsire nesnesi
     * @return İşlem başarılıysa true, değilse false
     * @throws SQLException Veritabanı hatası
     */
    public boolean create(Hemsire hemsire) throws SQLException {
        // SQL sorgusu `hemsire` tablosuna göre güncellendi
        String sql = "INSERT INTO hemsireler (personel_no, ad_soyad, tecrube_seviyesi, kurum_id, aktif, ise_giris_tarihi) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hemsire.getPersonelNo());
            ps.setString(2, hemsire.getAdSoyad());
            ps.setString(3, hemsire.getTecrubeSeviyesi());
            ps.setInt(4, hemsire.getKurumId());
            ps.setBoolean(5, hemsire.isAktif());
            ps.setDate(6, new Date(hemsire.getIseGirisTarihi().getTime()));

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Belirtilen tecrübe seviyesine sahip hemşire sayısını döndürür.
     * @param tecrubeSeviyesi Tecrübe seviyesi (örn: 'Tecrübeli', 'Tecrübesiz')
     * @return İlgili kategoriye ait hemşire sayısı
     * @throws SQLException Veritabanı hatası
     */
    public int getHemsireSayisiByKategori(String tecrubeSeviyesi) throws SQLException {
        String sql = "SELECT COUNT(*) FROM hemsireler WHERE tecrube_seviyesi = ? AND aktif = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tecrubeSeviyesi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Toplam aktif hemşire sayısını döndürür.
     * @return Toplam aktif hemşire sayısı
     * @throws SQLException Veritabanı hatası
     */
    public int getToplamHemsireSayisi() throws SQLException {
        String sql = "SELECT COUNT(*) FROM hemsireler WHERE aktif = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * ResultSet nesnesini Hemsire model nesnesine eşler.
     * @param rs ResultSet nesnesi
     * @return Hemsire nesnesi
     * @throws SQLException SQL Hatası
     */
    private Hemsire mapResultSetToHemsire(ResultSet rs) throws SQLException {
        Hemsire hemsire = new Hemsire();
        hemsire.setHemsireId(rs.getInt("hemsire_id"));
        hemsire.setPersonelNo(rs.getString("personel_no"));
        hemsire.setAdSoyad(rs.getString("ad_soyad"));
        hemsire.setTecrubeSeviyesi(rs.getString("tecrube_seviyesi"));
        hemsire.setKurumId(rs.getInt("kurum_id"));
        hemsire.setAktif(rs.getBoolean("aktif"));
        hemsire.setIseGirisTarihi(rs.getDate("ise_giris_tarihi"));
        // `tecrube` objesi artık kullanılmadığı için kaldırıldı.
        return hemsire;
    }


}