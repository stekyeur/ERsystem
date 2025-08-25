package com.hospital.dao;

import com.hospital.util.DatabaseConnection;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.KayitIslem;
import com.hospital.model.Rapor;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Veritabanındaki kayit_islem tablosuna erişim için kullanılan Data Access Object (DAO) sınıfı.
 */
public class KayitIslemDAO {

    /**
     * Günlük iş yükü özetini getirir.
     * Bu metot, her bir saat aralığındaki işlem sayısını ve ortalama bekleme süresini hesaplar.
     * Hata: "hasta_id" sütunu bulunamadı. Bu, "hasta_id" sütununun veritabanında mevcut olmadığını gösterir.
     * Çözüm: Sorguyu, hasta sayısını saymak yerine işlem sayısını sayacak şekilde değiştirdik.
     * @param tarih Analiz yapılacak günün tarihi.
     * @return Saat aralığına göre hasta sayısı ve bekleme süresi içeren özet listesi.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public List<IsYukuOzeti> getGunlukIsYukuOzeti(LocalDateTime tarih) throws SQLException {
        List<IsYukuOzeti> ozetler = new ArrayList<>();
        String sql = """
            SELECT 
                EXTRACT(HOUR FROM kayit_zamani) AS saat_araligi,
                COUNT(*) AS islem_sayisi,
                AVG(gercek_sure) AS ortalama_sure
            FROM kayit_islem 
            WHERE DATE(kayit_zamani) = DATE(?)
            GROUP BY saat_araligi
            ORDER BY saat_araligi
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(tarih));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int saat = (int) rs.getDouble("saat_araligi");
                String saatAraligi = String.format("%02d:00 - %02d:59", saat, saat);

                IsYukuOzeti ozet = new IsYukuOzeti(
                        saatAraligi,
                        rs.getInt("islem_sayisi"), // hasta_sayisi yerine islem_sayisi kullanıldı
                        (int) Math.round(rs.getDouble("ortalama_sure"))
                );
                ozetler.add(ozet);
            }
        }
        return ozetler;
    }

    /**
     * Yeni bir KayitIslem kaydını veritabanına ekler.
     *
     * @param kayitIslem Eklenecek KayitIslem nesnesi.
     * @return Yeni kaydın ID'si. Eğer ekleme başarısız olursa -1 döner.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public int insertKayitIslem(KayitIslem kayitIslem) throws SQLException {
        int insertedId = -1;
        // SQL sorgusu, otomatik oluşturulan id ve created_at kolonları hariç
        // diğer tüm alanları içerecek şekilde güncellendi.
        String sql = "INSERT INTO kayit_islem (kayit_zamani, hemsire_kategori, islem_id, birim_id, gercek_sure, hasta_durumu, notlar) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Parametrelerin sırası, SQL sorgusundaki kolon sırasına göre ayarlandı.
            ps.setTimestamp(1, Timestamp.valueOf(kayitIslem.getKayitZamani()));
            ps.setString(2, kayitIslem.getHemsireKategori());
            ps.setInt(3, kayitIslem.getIslemId());
            ps.setInt(4, kayitIslem.getBirimId());
            ps.setInt(5, kayitIslem.getGercekSure());
            ps.setString(6, kayitIslem.getHastaDurumu()); // hasta_durumu kolonu için ilgili değer kullanıldı
            ps.setString(7, kayitIslem.getNotlar());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        insertedId = rs.getInt(1);
                    }
                }
            }
        }
        return insertedId;
    }

    /**
     * Belirli bir birimdeki kayıtları belirli bir tarih aralığında getirir.
     * Hata: Veritabanı şemasında "hasta_id" sütunu bulunmadığı için ilgili satır kaldırıldı.
     * @param birimId Getirilecek kayıtların birim ID'si.
     * @param baslangic Sorgu için başlangıç tarihi.
     * @param bitis Sorgu için bitiş tarihi.
     * @return KayıtIslem nesnelerinin listesi.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public List<KayitIslem> getKayitlarByBirimId(int birimId, LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = "SELECT * FROM kayit_islem WHERE birim_id = ? AND kayit_zamani BETWEEN ? AND ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, birimId);
            ps.setTimestamp(2, Timestamp.valueOf(baslangic));
            ps.setTimestamp(3, Timestamp.valueOf(bitis));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KayitIslem kayit = new KayitIslem();
                kayit.setId(rs.getInt("id"));
                kayit.setBirimId(rs.getInt("birim_id"));
                // Veritabanında "hasta_id" sütunu bulunmadığından bu satır kaldırıldı
                // kayit.setHastaId(rs.getInt("hasta_id"));
                kayit.setIslemId(rs.getInt("islem_id"));
                kayit.setHemsireKategori(rs.getString("hemsire_kategori"));
                kayit.setKayitZamani(rs.getTimestamp("kayit_zamani").toLocalDateTime());
                kayit.setGercekSure(rs.getInt("gercek_sure"));
                kayitlar.add(kayit);
            }
        }
        return kayitlar;
    }

    public double getOrtalamaIslemSuresi(int islemId, String kategori, int sonGunSayisi) throws SQLException {
        double ortalama = 0.0;
        String sql = "SELECT AVG(gercek_sure) FROM kayit_islem WHERE islem_id = ? AND hemsire_kategori = ? AND kayit_zamani >= ?";

        LocalDateTime sonTarih = LocalDateTime.now().minusDays(sonGunSayisi);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, islemId);
            ps.setString(2, kategori);
            ps.setTimestamp(3, Timestamp.valueOf(sonTarih));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ortalama = rs.getDouble(1);
            }
        }
        return ortalama;
    }

    public List<KayitIslem> getKritikDurumKayitlari(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = new ArrayList<>();
        String sql = "SELECT * FROM kayit_islem WHERE kayit_zamani BETWEEN ? AND ? AND hasta_durumu = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(baslangic));
            ps.setTimestamp(2, Timestamp.valueOf(bitis));
            ps.setString(3, "kritik"); // Kritik durum için sabit bir değer gönderiyorum.
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KayitIslem kayit = new KayitIslem();
                kayit.setId(rs.getInt("id"));
                // ... diğer alanlar ...
                kayitlar.add(kayit);
            }
        }
        return kayitlar;
    }
}
