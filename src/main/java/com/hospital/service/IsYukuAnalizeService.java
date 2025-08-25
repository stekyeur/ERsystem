package com.hospital.service;

import com.hospital.dao.BirimDAO;
import com.hospital.dao.BirimIslemDAO;
import com.hospital.dao.KayitIslemDAO;
import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.dto.IslemOzeti; // Yeni eklenen DTO
import com.hospital.model.Birim;
import com.hospital.model.KayitIslem;
import com.hospital.util.DateUtils;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Bu sınıf, hastane acil servisindeki iş yükü ve hemşire ihtiyacı analizlerini
 * gerçekleştiren servis işlemlerini içerir.
 */
public class IsYukuAnalizeService {

    private KayitIslemDAO kayitIslemDAO;
    private BirimIslemDAO birimIslemDAO;
    private BirimDAO birimDAO;

    public IsYukuAnalizeService() {
        this.kayitIslemDAO = new KayitIslemDAO();
        this.birimIslemDAO = new BirimIslemDAO();
        this.birimDAO = new BirimDAO();
    }

    /**
     * Belirtilen tarih için günlük iş yükü analizini saatlik özetler halinde getirir.
     * @param tarih Analiz yapılacak günün tarihi.
     * @return Saat aralığına göre hasta sayısı ve bekleme süresi içeren özet listesi.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public List<IsYukuOzeti> getGunlukIsYukuAnalizi(LocalDateTime tarih) throws SQLException {
        return kayitIslemDAO.getGunlukIsYukuOzeti(tarih);
    }

    /**
     * Belirtilen tarih aralığı için birim bazlı toplam iş yüküne göre
     * hemşire ihtiyacını hesaplar.
     * Bu metot, her gün için ayrı bir hemşire ihtiyacı kaydı oluşturur.
     * @param baslangic Analiz yapılacak başlangıç tarihi.
     * @param bitis Analiz yapılacak bitiş tarihi.
     * @return Her gün için gerekli ve mevcut hemşire sayısını içeren liste.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public List<HemsireIhtiyaci> hesaplaHemsireIhtiyaci(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<HemsireIhtiyaci> ihtiyaclar = new ArrayList<>();
        List<Birim> birimler = birimDAO.getAktifBirimler();

        for (LocalDateTime tarih = baslangic; !tarih.isAfter(bitis); tarih = tarih.plusDays(1)) {

            int toplamIsYukuDk = 0;

            for (Birim birim : birimler) {
                List<KayitIslem> birimKayitlari = kayitIslemDAO.getKayitlarByBirimId(birim.getId(), tarih, tarih);

                toplamIsYukuDk += birimKayitlari.stream()
                        .mapToInt(KayitIslem::getGercekSure)
                        .sum();
            }

            int gerekliHemsire = (int) Math.ceil(toplamIsYukuDk / 480.0);
            int mevcutHemsire = 5;

            HemsireIhtiyaci ihtiyac = new HemsireIhtiyaci();
            ihtiyac.setTarih(Date.valueOf(tarih.toLocalDate()));
            ihtiyac.setGerekliHemsire(gerekliHemsire);
            ihtiyac.setMevcutHemsire(mevcutHemsire);

            ihtiyaclar.add(ihtiyac);
        }

        return ihtiyaclar;
    }

    /**
     * Bu metot, özel raporlar için iş yükü analizini tarih aralığına göre yapar.
     * Her bir saat aralığı için bekleme süresi ve hasta sayısı gibi verileri içerir.
     * @param baslangic Analiz yapılacak başlangıç tarihi.
     * @param bitis Analiz yapılacak bitiş tarihi.
     * @return Saatlik iş yükü özetlerini içeren liste.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public List<IsYukuOzeti> getIsYukuOzetiForRange(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        return kayitIslemDAO.getGunlukIsYukuOzeti(baslangic);
    }


    /**
     * Belirli bir işlem için, son N gün içerisindeki ortalama işlem süresini kategorilere göre analiz eder.
     * @param islemId Analiz edilecek işlemin ID'si.
     * @param sonGunSayisi Analizin yapılacağı gün sayısı.
     * @return Kategoriye göre ortalama süreleri içeren harita.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public Map<String, Double> getIslemSuresiAnalizi(int islemId, int sonGunSayisi) throws SQLException {
        Map<String, Double> analiz = new java.util.HashMap<>();

        String[] kategoriler = {"t1", "t2", "t3"};
        for (String kategori : kategoriler) {
            double ortalamaSure = kayitIslemDAO.getOrtalamaIslemSuresi(islemId, kategori, sonGunSayisi);
            analiz.put(kategori, ortalamaSure);
        }

        return analiz;
    }

    // Kritik durumlar için acil kaynak ihtiyacı
    public List<KayitIslem> getKritikDurumAnalizi(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        return kayitIslemDAO.getKritikDurumKayitlari(baslangic, bitis);
    }

    /**
     * Direct ve Indirect iş dağılımı analizi yapar.
     * @param birimId Analiz yapılacak birim ID'si.
     * @param baslangic Analiz yapılacak başlangıç tarihi.
     * @param bitis Analiz yapılacak bitiş tarihi.
     * @return Direct ve Indirect işler için işlem özeti içeren harita.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public Map<String, IslemOzeti> getDirectIndirectAnalizi(int birimId, LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = kayitIslemDAO.getKayitlarByBirimId(birimId, baslangic, bitis);

        int directToplamSure = 0;
        int directToplamIslem = 0;

        int indirectToplamSure = 0;
        int indirectToplamIslem = 0;

        for (KayitIslem kayit : kayitlar) {
            // isDirect() method'unun KayitIslem sınıfında mevcut olduğu varsayımı
            if (kayit.getIslem().isDirect()) {
                directToplamSure += kayit.getGercekSure();
                directToplamIslem++;
            } else {
                indirectToplamSure += kayit.getGercekSure();
                indirectToplamIslem++;
            }
        }

        Map<String, IslemOzeti> analiz = new java.util.HashMap<>();
        analiz.put("direct", new IslemOzeti("Direct İşlemler", directToplamIslem, directToplamSure));
        analiz.put("indirect", new IslemOzeti("Indirect İşlemler", indirectToplamIslem, indirectToplamSure));

        return analiz;
    }

    /**
     * Vardiya bazlı iş yükü dağılımını analiz eder.
     * @param birimId Analiz yapılacak birim ID'si.
     * @param tarih Analiz yapılacak günün tarihi.
     * @return Vardiya bazlı işlem özeti içeren harita.
     * @throws SQLException Veritabanı erişim hatası durumunda fırlatılır.
     */
    public Map<String, IslemOzeti> getVardiyaBazliAnaliz(int birimId, LocalDateTime tarih) throws SQLException {
        LocalDateTime gunBaslangic = tarih.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime gunBitis = tarih.withHour(23).withMinute(59).withSecond(59);

        List<KayitIslem> gunlukKayitlar = kayitIslemDAO.getKayitlarByBirimId(birimId, gunBaslangic, gunBitis);

        Map<String, IslemOzeti> vardiyaAnalizi = new java.util.HashMap<>();

        String[] vardiyalar = {"sabah", "aksam", "gece"};
        int[][] vardiyaSaatleri = {{8, 16}, {16, 24}, {0, 8}};

        for (int i = 0; i < vardiyalar.length; i++) {
            String vardiya = vardiyalar[i];
            int baslangicSaat = vardiyaSaatleri[i][0];
            int bitisSaat = vardiyaSaatleri[i][1];

            List<KayitIslem> vardiyaKayitlari = gunlukKayitlar.stream()
                    .filter(k -> {
                        int saat = k.getKayitZamani().getHour();
                        if (baslangicSaat < bitisSaat) {
                            return saat >= baslangicSaat && saat < bitisSaat;
                        } else {
                            return saat >= baslangicSaat || saat < bitisSaat;
                        }
                    })
                    .collect(Collectors.toList());

            int toplamSure = vardiyaKayitlari.stream().mapToInt(KayitIslem::getGercekSure).sum();
            int toplamIslem = vardiyaKayitlari.size();

            vardiyaAnalizi.put(vardiya, new IslemOzeti(vardiya + " Vardiyası", toplamIslem, toplamSure));
        }

        return vardiyaAnalizi;
    }
}
