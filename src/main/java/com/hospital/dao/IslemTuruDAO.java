package com.hospital.dao;

import com.hospital.model.IslemTuru;
import com.hospital.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IslemTuruDAO {

    public List<IslemTuru> findAll() {
        List<IslemTuru> islemler = new ArrayList<>();
        String sql = "SELECT * FROM acil_islem_turleri WHERE aktif = 1 ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                islemler.add(mapResultSetToIslemTuru(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    public IslemTuru findById(int id) {
        String sql = "SELECT * FROM acil_islem_turleri WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToIslemTuru(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<IslemTuru> findByTip(String islemTipi) {
        List<IslemTuru> islemler = new ArrayList<>();
        String sql = "SELECT * FROM acil_islem_turleri WHERE aktif = 1 AND islem_tipi = ? ORDER BY islem_adi";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, islemTipi);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                islemler.add(mapResultSetToIslemTuru(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return islemler;
    }

    private IslemTuru mapResultSetToIslemTuru(ResultSet rs) throws SQLException {
        IslemTuru islem = new IslemTuru();
        islem.setId(rs.getInt("id"));
        islem.setIslemKodu(rs.getString("islem_kodu"));
        islem.setIslemAdi(rs.getString("islem_adi"));
        islem.setIslemTipi(rs.getString("islem_tipi"));
        islem.setStandartSureDk(rs.getInt("standart_sure_dk"));
        islem.setAciklama(rs.getString("aciklama"));
        islem.setAktif(rs.getBoolean("aktif"));
        return islem;
    }
}
