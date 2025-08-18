package com.hospital.service;


import com.hospital.dao.BirimDAO;
import com.hospital.dao.BirimIslemDAO;
import com.hospital.dao.KayitIslemDAO;
import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.Birim;
import com.hospital.model.KayitIslem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IsYukuAnalizeService {

    private KayitIslemDAO kayitIslemDAO;
    private BirimIslemDAO birimIslemDAO;
    private BirimDAO birimDAO;

    public IsYukuAnalizeService() {
        this.kayitIslemDAO = new KayitIslemDAO();
        this.birimIslemDAO = new BirimIslemDAO();
        this.birimDAO = new BirimDAO();
    }

    // Günlük iş yükü analizi
    public List<IsYukuOzeti> getGunlukIsYukuAnalizi(LocalDateTime tarih) throws SQLException {
        return kayitIslemDAO.getGunlukIsYukuOzeti(tarih);
    }

    // Birim bazlı hemşire ihtiyacı hesaplama
    public List<HemsireIhtiyaci> hesaplaHemsireIhtiyaci(LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<HemsireIhtiyaci> ihtiyaclar = new ArrayList<>();
        List<Birim> birimler = birimDAO.getAktifBirimler();

        for (Birim birim : birimler) {
            // Her hemşire kategorisi için ayrı analiz
            String[] kategoriler = {"t1", "t2", "t3"};

            for (String kategori : kategoriler) {
                List<KayitIslem> birimKayitlari = kayitIslemDAO.getKayitlarByBirimId(birim.getId(), baslangic, bitis);

                // Bu kategorideki kayıtları filtrele
                List<KayitIslem> kategoriKayitlari = birimKayitlari.stream()
                        .filter(k -> kategori.equals(k.getHemsireKategori()))
                        .collect(Collectors.toList());

                if (!kategoriKayitlari.isEmpty()) {
                    // Toplam iş yükü hesapla
                    int toplamSureDk = kategoriKayitlari.stream()
                            .mapToInt(KayitIslem::getGercekSure)
                            .sum();

                    double gunlukIsYukuSaat = toplamSureDk / 60.0;

                    HemsireIhtiyaci ihtiyac = new HemsireIhtiyaci(birim.getBirimAdi(), kategori, gunlukIsYukuSaat);
                    // Mevcut hemşire sayısı ayrı bir tablodan gelecek, şimdilik default değer
                    ihtiyac.setMevcutHemsireSayisi(2);

                    ihtiyaclar.add(ihtiyac);
                }
            }
        }

        return ihtiyaclar;
    }

    // İşlem süresi optimizasyon önerileri
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

    // Direct vs Indirect iş dağılımı analizi
    public Map<String, IsYukuOzeti> getDirectIndirectAnalizi(int birimId, LocalDateTime baslangic, LocalDateTime bitis) throws SQLException {
        List<KayitIslem> kayitlar = kayitIslemDAO.getKayitlarByBirimId(birimId, baslangic, bitis);

        // Direct işlemler
        int directToplamSure = 0;
        int directToplamIslem = 0;

        // Indirect işlemler
        int indirectToplamSure = 0;
        int indirectToplamIslem = 0;

        for (KayitIslem kayit : kayitlar) {
            if (kayit.getIslem().isDirect()) {
                directToplamSure += kayit.getGercekSure();
                directToplamIslem++;
            } else {
                indirectToplamSure += kayit.getGercekSure();
                indirectToplamIslem++;
            }
        }

        Map<String, IsYukuOzeti> analiz = new java.util.HashMap<>();
        analiz.put("direct", new IsYukuOzeti("Direct İşlemler", "Tüm Kategoriler", directToplamIslem, directToplamSure));
        analiz.put("indirect", new IsYukuOzeti("Indirect İşlemler", "Tüm Kategoriler", indirectToplamIslem, indirectToplamSure));

        return analiz;
    }

    // Vardiya bazlı iş yükü dağılımı
    public Map<String, IsYukuOzeti> getVardiyaBazliAnaliz(int birimId, LocalDateTime tarih) throws SQLException {
        LocalDateTime gunBaslangic = tarih.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime gunBitis = tarih.withHour(23).withMinute(59).withSecond(59);

        List<KayitIslem> gunlukKayitlar = kayitIslemDAO.getKayitlarByBirimId(birimId, gunBaslangic, gunBitis);

        Map<String, IsYukuOzeti> vardiyaAnalizi = new java.util.HashMap<>();

        // Vardiya saatleri: Sabah (08:00-16:00), Akşam (16:00-00:00), Gece (00:00-08:00)
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

            vardiyaAnalizi.put(vardiya, new IsYukuOzeti(vardiya + " Vardiyası", "Tüm Kategoriler", toplamIslem, toplamSure));
        }

        return vardiyaAnalizi;
    }
}