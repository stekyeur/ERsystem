package com.hospital.dao;

import com.hospital.model.Hemsire;
import com.hospital.model.HemsireTecrube;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HemsireDAO {

    public List<Hemsire> findAll() {
        List<Hemsire> hemsireler = new ArrayList<>();
        String sql = "SELECT hemsire_id, personel_no, ad_soyad, tecrube_seviyesi, kurum_id, aktif, " +
                "ise_giris_tarihi, olusturma_tarihi, guncelleme_tarihi " +
                "FROM hemsireler WHERE aktif = TRUE ORDER BY ad_soyad";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                hemsireler.add(mapResultSetToHemsire(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hemsireler;
    }

    public Hemsire findById(int id) {
        String sql = "SELECT hemsire_id, personel_no, ad_soyad, tecrube_seviyesi, kurum_id, aktif, " +
                "ise_giris_tarihi, olusturma_tarihi, guncelleme_tarihi " +
                "FROM hemsireler WHERE hemsire_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToHemsire(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(Hemsire hemsire) {
        String sql = "INSERT INTO hemsireler (personel_no, ad_soyad, tecrube_seviyesi, kurum_id, aktif, ise_giris_tarihi) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hemsire.getPersonelNo());
            ps.setString(2, hemsire.getAdSoyad());
            ps.setString(3, hemsire.getTecrubeSeviyesi());
            ps.setInt(4, hemsire.getKurumId());
            ps.setBoolean(5, hemsire.isAktif());

            if (hemsire.getIseGirisTarihi() != null) {
                ps.setDate(6, new Date(hemsire.getIseGirisTarihi().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Hemsire mapResultSetToHemsire(ResultSet rs) throws SQLException {
        Hemsire hemsire = new Hemsire();
        hemsire.setHemsireId(rs.getInt("hemsire_id"));
        hemsire.setPersonelNo(rs.getString("personel_no"));
        hemsire.setAdSoyad(rs.getString("ad_soyad"));
        hemsire.setTecrubeSeviyesi(rs.getString("tecrube_seviyesi"));
        hemsire.setKurumId(rs.getInt("kurum_id"));
        hemsire.setAktif(rs.getBoolean("aktif"));
        hemsire.setIseGirisTarihi(rs.getDate("ise_giris_tarihi"));
        hemsire.setOlusturmaTarihi(rs.getTimestamp("olusturma_tarihi"));
        hemsire.setGuncellemeTarihi(rs.getTimestamp("guncelleme_tarihi"));
        return hemsire;
    }
}
