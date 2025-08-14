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
        String sql = "SELECT h.*, ht.kategori_kodu, ht.kategori_adi " +
                "FROM acil_hemsire h " +
                "LEFT JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "WHERE h.aktif = 1 ORDER BY h.ad_soyad";

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
        String sql = "SELECT h.*, ht.kategori_kodu, ht.kategori_adi " +
                "FROM acil_hemsire h " +
                "LEFT JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "WHERE h.id = ?";

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
        String sql = "INSERT INTO acil_hemsire (sicil_no, ad_soyad, tecrube_id, bolum, telefon, email, ise_giris_tarihi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hemsire.getSicilNo());
            ps.setString(2, hemsire.getAdSoyad());
            ps.setInt(3, hemsire.getTecrubeId());
            ps.setString(4, hemsire.getBolum());
            ps.setString(5, hemsire.getTelefon());
            ps.setString(6, hemsire.getEmail());
            ps.setDate(7, (Date) hemsire.getIseGirisTarihi());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Hemsire> findByTecrubeKategorisi(String kategoriKodu) {
        List<Hemsire> hemsireler = new ArrayList<>();
        String sql = "SELECT h.*, ht.kategori_kodu, ht.kategori_adi " +
                "FROM acil_hemsire h " +
                "INNER JOIN acil_hemsire_tecrube ht ON h.tecrube_id = ht.id " +
                "WHERE h.aktif = 1 AND ht.kategori_kodu = ? ORDER BY h.ad_soyad";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kategoriKodu);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                hemsireler.add(mapResultSetToHemsire(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hemsireler;
    }

    private Hemsire mapResultSetToHemsire(ResultSet rs) throws SQLException {
        Hemsire hemsire = new Hemsire();
        hemsire.setId(rs.getInt("id"));
        hemsire.setSicilNo(rs.getString("sicil_no"));
        hemsire.setAdSoyad(rs.getString("ad_soyad"));
        hemsire.setTecrubeId(rs.getInt("tecrube_id"));
        hemsire.setBolum(rs.getString("bolum"));
        hemsire.setTelefon(rs.getString("telefon"));
        hemsire.setEmail(rs.getString("email"));
        hemsire.setIseGirisTarihi(rs.getDate("ise_giris_tarihi"));
        hemsire.setAktif(rs.getBoolean("aktif"));

        // Tecrübe bilgisi
        if (rs.getString("kategori_kodu") != null) {
            HemsireTecrube tecrube = new HemsireTecrube();
            tecrube.setKategoriKodu(rs.getString("kategori_kodu"));
            tecrube.setKategoriAdi(rs.getString("kategori_adi"));
            hemsire.setTecrube(tecrube);
        }

        return hemsire;
    }
}