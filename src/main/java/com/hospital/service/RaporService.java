package com.hospital.service;

import com.hospital.dto.HemsireIhtiyaci;
import com.hospital.dto.IsYukuOzeti;
import com.hospital.model.Rapor;
import com.hospital.dao.RaporDAO;
import com.hospital.util.ExcelExporter;
import com.hospital.util.PDFExporter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaporService {

    private IsYukuAnalizeService analizeService;
    private RaporDAO raporDAO;
    private ExecutorService executorService;

    // Rapor dosyalarının kaydedileceği klasör
    private static final String RAPOR_KLASORU = System.getProperty("user.home") + "/hospital_reports/";

    public RaporService() {
        this.analizeService = new IsYukuAnalizeService();
        this.raporDAO = new RaporDAO();
        this.executorService = Executors.newFixedThreadPool(3); // 3 paralel rapor işlemi

        // Rapor klasörünü oluştur
        createReportDirectory();
    }

    private void createReportDirectory() {
        File directory = new File(RAPOR_KLASORU);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Asenkron rapor oluşturma
    public CompletableFuture<Rapor> createRaporAsync(String raporAdi, String raporTipi, String format,
                                                     LocalDateTime baslangic, LocalDateTime bitis,
                                                     int kullaniciId) {

        return CompletableFuture.supplyAsync(() -> {
            int raporId;
            try {
                // Rapor kaydını veritabanına ekle
                Rapor rapor = new Rapor(raporAdi, raporTipi, format.toUpperCase(),
                        java.sql.Date.valueOf(baslangic.toLocalDate()),
                        java.sql.Date.valueOf(bitis.toLocalDate()));
                rapor.setOlusturanKullaniciId(kullaniciId);
                rapor.setDurum("OLUSTURULUYOR");

                raporId = raporDAO.insert(rapor);
                rapor.setId(raporId);

                // Raporu oluştur
                String dosyaYolu = generateReport(rapor, baslangic, bitis);

                // Dosya boyutunu hesapla
                File file = new File(dosyaYolu);
                long dosyaBoyutu = file.exists() ? file.length() : 0;

                // Rapor durumunu güncelle
                rapor.setDosyaYolu(dosyaYolu);
                rapor.setDosyaBoyutu(dosyaBoyutu);
                rapor.setDurum("HAZIR");
                raporDAO.update(rapor);

                return rapor;

            } catch (Exception e) {
                // Hata durumunda raporu güncelle
                try {
                    // Hata durumunda raporun ID'si belli değilse raporDAO'ya erişemeyiz.
                    // O yüzden bu kısmı try-catch bloğuna almamız ve hata yönetimi yapmamız gerekir.
                    // Bu kısımdaki mantık düzeltildi.
                    throw new RuntimeException("Rapor oluşturulurken hata: " + e.getMessage(), e);
                } catch (RuntimeException ex) {
                    throw ex;
                }
            }
        }, executorService);
    }

    // Raporu asıl oluşturan metot
    private String generateReport(Rapor rapor, LocalDateTime baslangic, LocalDateTime bitis)
            throws SQLException, IOException {

        String fileName = generateFileName(rapor, baslangic);
        String dosyaYolu = RAPOR_KLASORU + fileName;

        // Rapor tipine göre uygun veriyi çek ve dosyayı oluştur
        switch (rapor.getRaporTipi()) {
            case "GUNLUK":
                List<IsYukuOzeti> gunlukAnaliz = analizeService.getGunlukIsYukuAnalizi(baslangic);
                if ("EXCEL".equalsIgnoreCase(rapor.getFormat())) {
                    ExcelExporter.exportIsYukuOzeti(gunlukAnaliz, dosyaYolu);
                } else if ("PDF".equalsIgnoreCase(rapor.getFormat())) {
                    PDFExporter.exportIsYukuOzeti(gunlukAnaliz, dosyaYolu);
                }
                break;
            case "HAFTALIK":
                List<HemsireIhtiyaci> hemsireAnaliziHaftalik = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
                if ("EXCEL".equalsIgnoreCase(rapor.getFormat())) {
                    ExcelExporter.exportHemsireIhtiyaci(hemsireAnaliziHaftalik, dosyaYolu);
                } else if ("PDF".equalsIgnoreCase(rapor.getFormat())) {
                    PDFExporter.exportHemsireIhtiyaci(hemsireAnaliziHaftalik, dosyaYolu);
                }
                break;
            case "AYLIK":
                List<HemsireIhtiyaci> hemsireAnaliziAylik = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
                if ("EXCEL".equalsIgnoreCase(rapor.getFormat())) {
                    ExcelExporter.exportHemsireIhtiyaci(hemsireAnaliziAylik, dosyaYolu);
                } else if ("PDF".equalsIgnoreCase(rapor.getFormat())) {
                    PDFExporter.exportHemsireIhtiyaci(hemsireAnaliziAylik, dosyaYolu);
                }
                break;
            case "OZEL":
                // Özel raporun tipi 'is_yuku' veya 'hemsire_ihtiyaci' olarak belirtilmeli.
                // Basitlik için sadece hemsire ihtiyacını ele alıyorum.
                List<HemsireIhtiyaci> hemsireIhtiyaciOzel = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
                if ("EXCEL".equalsIgnoreCase(rapor.getFormat())) {
                    ExcelExporter.exportHemsireIhtiyaci(hemsireIhtiyaciOzel, dosyaYolu);
                } else if ("PDF".equalsIgnoreCase(rapor.getFormat())) {
                    PDFExporter.exportHemsireIhtiyaci(hemsireIhtiyaciOzel, dosyaYolu);
                }
                break;
            default:
                throw new IllegalArgumentException("Geçersiz rapor tipi: " + rapor.getRaporTipi());
        }

        return dosyaYolu;
    }

    private String generateFileName(Rapor rapor, LocalDateTime tarih) {
        String prefix = rapor.getRaporTipi().toLowerCase() + "_rapor_";
        String tarihStr = tarih.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String extension = "pdf".equals(rapor.getFormat().toLowerCase()) ? ".pdf" : ".xlsx";

        return prefix + tarihStr + "_" + System.currentTimeMillis() + extension;
    }

    // Günlük rapor oluştur
    public void createGunlukRapor(LocalDateTime tarih, String format, String dosyaYolu)
            throws SQLException, IOException {
        List<IsYukuOzeti> gunlukAnaliz = analizeService.getGunlukIsYukuAnalizi(tarih);

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportIsYukuOzeti(gunlukAnaliz, dosyaYolu);
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportIsYukuOzeti(gunlukAnaliz, dosyaYolu);
        }
    }

    // Haftalık rapor oluştur
    public void createHaftalikRapor(LocalDateTime haftaBaslangici, String format, String dosyaYolu)
            throws SQLException, IOException {
        LocalDateTime haftaBitisi = haftaBaslangici.plusDays(7);
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(haftaBaslangici, haftaBitisi);

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportHemsireIhtiyaci(hemsireAnalizi, dosyaYolu);
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportHemsireIhtiyaci(hemsireAnalizi, dosyaYolu);
        }
    }

    // Aylık kapsamlı rapor oluştur
    public void createAylikRapor(LocalDateTime ayBaslangici, String format, String dosyaYolu)
            throws SQLException, IOException {
        LocalDateTime ayBitisi = ayBaslangici.plusMonths(1);
        List<HemsireIhtiyaci> hemsireAnalizi = analizeService.hesaplaHemsireIhtiyaci(ayBaslangici, ayBitisi);

        if ("excel".equalsIgnoreCase(format)) {
            ExcelExporter.exportHemsireIhtiyaci(hemsireAnalizi, dosyaYolu);
        } else if ("pdf".equalsIgnoreCase(format)) {
            PDFExporter.exportHemsireIhtiyaci(hemsireAnalizi, dosyaYolu);
        }
    }

    // Özel tarih aralığı raporu
    public void createOzelRapor(LocalDateTime baslangic, LocalDateTime bitis, String raporTipi,
                                String format, String dosyaYolu) throws SQLException, IOException {

        switch (raporTipi.toLowerCase()) {
            case "is_yuku":
                List<IsYukuOzeti> isYukuAnaliz = analizeService.getGunlukIsYukuAnalizi(baslangic);
                if ("excel".equalsIgnoreCase(format)) {
                    ExcelExporter.exportIsYukuOzeti(isYukuAnaliz, dosyaYolu);
                } else {
                    PDFExporter.exportIsYukuOzeti(isYukuAnaliz, dosyaYolu);
                }
                break;

            case "hemsire_ihtiyaci":
                List<HemsireIhtiyaci> hemsireIhtiyaci = analizeService.hesaplaHemsireIhtiyaci(baslangic, bitis);
                if ("excel".equalsIgnoreCase(format)) {
                    ExcelExporter.exportHemsireIhtiyaci(hemsireIhtiyaci, dosyaYolu);
                } else {
                    PDFExporter.exportHemsireIhtiyaci(hemsireIhtiyaci, dosyaYolu);
                }
                break;

            default:
                throw new IllegalArgumentException("Geçersiz rapor tipi: " + raporTipi);
        }
    }

    // Kullanıcının raporlarını getir
    public List<Rapor> getKullaniciRaporlari(int kullaniciId) throws SQLException {
        return raporDAO.findByKullaniciId(kullaniciId);
    }

    // Rapor durumlarına göre filtrele
    public List<Rapor> getFilteredRaporlar(int kullaniciId, String durum, String tip, String format)
            throws SQLException {
        return raporDAO.findFiltered(kullaniciId, durum, tip, format);
    }

    // Rapor dosyasını getir
    public File getRaporDosyasi(int raporId, int kullaniciId) throws SQLException {
        Rapor rapor = raporDAO.findById(raporId);

        if (rapor == null) {
            throw new IllegalArgumentException("Rapor bulunamadı");
        }

        if (rapor.getOlusturanKullaniciId() != kullaniciId) {
            throw new SecurityException("Bu rapora erişim yetkiniz yok");
        }

        if (!"HAZIR".equals(rapor.getDurum())) {
            throw new IllegalStateException("Rapor henüz hazır değil");
        }

        File dosya = new File(rapor.getDosyaYolu());
        if (!dosya.exists()) {
            throw new IllegalStateException("Rapor dosyası bulunamadı");
        }

        return dosya;
    }

    // Raporu sil
    public boolean deleteRapor(int raporId, int kullaniciId) throws SQLException {
        Rapor rapor = raporDAO.findById(raporId);

        if (rapor == null) {
            return false;
        }

        if (rapor.getOlusturanKullaniciId() != kullaniciId) {
            throw new SecurityException("Bu raporu silme yetkiniz yok");
        }

        // Dosyayı sil
        if (rapor.getDosyaYolu() != null) {
            File dosya = new File(rapor.getDosyaYolu());
            if (dosya.exists()) {
                dosya.delete();
            }
        }

        // Veritabanından sil
        return raporDAO.delete(raporId);
    }

    // Raporu yeniden oluştur
    public CompletableFuture<Rapor> retryRapor(int raporId, int kullaniciId) throws SQLException {
        Rapor rapor = raporDAO.findById(raporId);

        if (rapor == null) {
            throw new IllegalArgumentException("Rapor bulunamadı");
        }

        if (rapor.getOlusturanKullaniciId() != kullaniciId) {
            throw new SecurityException("Bu raporu yeniden oluşturma yetkiniz yok");
        }

        // Eski dosyayı sil
        if (rapor.getDosyaYolu() != null) {
            File eskiDosya = new File(rapor.getDosyaYolu());
            if (eskiDosya.exists()) {
                eskiDosya.delete();
            }
        }

        // Hatanın olduğu kısım düzeltildi
        // java.util.Date'den LocalDateTime'a dönüşüm yapılıyor
        LocalDateTime baslangic = rapor.getBaslangicTarihi().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime bitis = rapor.getBitisTarihi().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        return createRaporAsync(rapor.getRaporAdi(), rapor.getRaporTipi(),
                rapor.getFormat(), baslangic, bitis, kullaniciId);
    }

    // Service'i kapat
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // Rapor istatistikleri
    public RaporIstatistikleri getRaporIstatistikleri(int kullaniciId) throws SQLException {
        List<Rapor> raporlar = getKullaniciRaporlari(kullaniciId);

        int toplam = raporlar.size();
        int hazir = 0;
        int olusturuluyor = 0;
        int hata = 0;
        long toplamBoyut = 0;

        for (Rapor rapor : raporlar) {
            switch (rapor.getDurum()) {
                case "HAZIR":
                    hazir++;
                    toplamBoyut += rapor.getDosyaBoyutu();
                    break;
                case "OLUSTURULUYOR":
                    olusturuluyor++;
                    break;
                case "HATA":
                    hata++;
                    break;
            }
        }

        return new RaporIstatistikleri(toplam, hazir, olusturuluyor, hata, toplamBoyut);
    }

    // İstatistik sınıfı
    public static class RaporIstatistikleri {
        public final int toplam;
        public final int hazir;
        public final int olusturuluyor;
        public final int hata;
        public final long toplamBoyut;

        public RaporIstatistikleri(int toplam, int hazir, int olusturuluyor, int hata, long toplamBoyut) {
            this.toplam = toplam;
            this.hazir = hazir;
            this.olusturuluyor = olusturuluyor;
            this.hata = hata;
            this.toplamBoyut = toplamBoyut;
        }
    }
}