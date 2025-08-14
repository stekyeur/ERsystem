package com.hospital.dao;

import com.hospital.model.Birim;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BirimDAO {

    public List<Birim> findAll() {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT * FROM acil_birimler WHERE aktif = 1 ORDER BY birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birimler;
    }

    public Birim findById(int id) {
        String sql = "SELECT * FROM acil_birimler WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToBirim(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Birim> findByIslemId(int islemId) {
        List<Birim> birimler = new ArrayList<>();
        String sql = "SELECT b.* FROM acil_birimler b " +
                "INNER JOIN acil_islem_birim_iliski ibi ON b.id = ibi.birim_id " +
                "WHERE b.aktif = 1 AND ibi.islem_id = ? ORDER BY b.birim_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, islemId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                birimler.add(mapResultSetToBirim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return birimler;
    }

    private Birim mapResultSetToBirim(ResultSet rs) throws SQLException {
        Birim birim = new Birim();
        birim.setId(rs.getInt("id"));
        birim.setBirimKodu(rs.getString("birim_kodu"));
        birim.setBirimAdi(rs.getString("birim_adi"));
        birim.setKapasite(rs.getInt("kapasite"));
        birim.setAciklama(rs.getString("aciklama"));
        birim.setAktif(rs.getBoolean("aktif"));
        return birim;
    }
}
