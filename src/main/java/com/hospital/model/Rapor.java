package com.hospital.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rapor {
    private int id;
    private String raporAdi;
    private String raporTipi; // GUNLUK, HAFTALIK, AYLIK, OZEL
    private String format; // PDF, EXCEL
    private String dosyaYolu;
    private Timestamp olusturmaTarihi;
    private int olusturanKullaniciId;
    private Date baslangicTarihi;
    private Date bitisTarihi;
    private String durum; // OLUSTURULUYOR, HAZIR, HATA
    private String aciklama; // Rapor açıklaması
    private long dosyaBoyutu; // Byte cinsinden

    // İlişkili objeler
    private Kullanici olusturanKullanici;

    // Constructors
    public Rapor() {
        this.olusturmaTarihi = new Timestamp(System.currentTimeMillis());
        this.durum = "OLUSTURULUYOR";
    }

    public Rapor(String raporAdi, String raporTipi, String format, Date baslangicTarihi, Date bitisTarihi) {
        this();
        this.raporAdi = raporAdi;
        this.raporTipi = raporTipi;
        this.format = format;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
    }

    // Utility methods
    public boolean isHazir() {
        return "HAZIR".equals(this.durum);
    }

    public boolean isHataVar() {
        return "HATA".equals(this.durum);
    }

    public boolean isOlusturuluyor() {
        return "OLUSTURULUYOR".equals(this.durum);
    }

    public String getDurumText() {
        if (this.durum == null) return "Bilinmiyor";
        switch (this.durum) {
            case "HAZIR": return "Hazır";
            case "HATA": return "Hata";
            case "OLUSTURULUYOR": return "Oluşturuluyor";
            default: return "Bilinmiyor";
        }
    }

    public String getRaporTipiText() {
        if (this.raporTipi == null) return "Bilinmiyor";
        switch (this.raporTipi) {
            case "GUNLUK": return "Günlük";
            case "HAFTALIK": return "Haftalık";
            case "AYLIK": return "Aylık";
            case "OZEL": return "Özel Tarih Aralığı";
            default: return this.raporTipi;
        }
    }

    public String getFormatText() {
        return this.format != null ? this.format.toUpperCase() : "";
    }

    public String getDosyaBoyutuText() {
        if (dosyaBoyutu <= 0) return "";

        if (dosyaBoyutu < 1024) {
            return dosyaBoyutu + " B";
        } else if (dosyaBoyutu < 1024 * 1024) {
            return String.format("%.1f KB", dosyaBoyutu / 1024.0);
        } else {
            return String.format("%.1f MB", dosyaBoyutu / (1024.0 * 1024.0));
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRaporAdi() { return raporAdi; }
    public void setRaporAdi(String raporAdi) { this.raporAdi = raporAdi; }

    public String getRaporTipi() { return raporTipi; }
    public void setRaporTipi(String raporTipi) { this.raporTipi = raporTipi; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getDosyaYolu() { return dosyaYolu; }
    public void setDosyaYolu(String dosyaYolu) { this.dosyaYolu = dosyaYolu; }

    public Timestamp getOlusturmaTarihi() { return olusturmaTarihi; }
    public void setOlusturmaTarihi(Timestamp olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }

    public int getOlusturanKullaniciId() { return olusturanKullaniciId; }
    public void setOlusturanKullaniciId(int olusturanKullaniciId) { this.olusturanKullaniciId = olusturanKullaniciId; }

    public Date getBaslangicTarihi() { return baslangicTarihi; }
    public void setBaslangicTarihi(Date baslangicTarihi) { this.baslangicTarihi = baslangicTarihi; }

    public Date getBitisTarihi() { return bitisTarihi; }
    public void setBitisTarihi(Date bitisTarihi) { this.bitisTarihi = bitisTarihi; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public long getDosyaBoyutu() { return dosyaBoyutu; }
    public void setDosyaBoyutu(long dosyaBoyutu) { this.dosyaBoyutu = dosyaBoyutu; }

    public Kullanici getOlusturanKullanici() { return olusturanKullanici; }
    public void setOlusturanKullanici(Kullanici olusturanKullanici) { this.olusturanKullanici = olusturanKullanici; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String olusturmaTarihiStr = (olusturmaTarihi != null) ? sdf.format(olusturmaTarihi) : "null";
        return String.format("Rapor{id=%d, adi='%s', tip='%s', format='%s', durum='%s', olusturmaTarihi='%s'}",
                id, raporAdi, raporTipi, format, durum, olusturmaTarihiStr);
    }
}