package com.hospital.model;

import java.sql.Timestamp;
import java.util.Date;

public class Rapor {
    private int id;
    private String raporAdi;
    private String raporTipi; // GUNLUK, AYLIK, YILLIK
    private String format; // PDF, EXCEL
    private String dosyaYolu;
    private Timestamp olusturmaTarihi;
    private int olusturanKullaniciId;
    private Date baslangicTarihi;
    private Date bitisTarihi;
    private String durum; // OLUSTURULUYOR, HAZIR, HATA

    // İlişkili objeler
    private Kullanici olusturanKullanici;

    // Constructors
    public Rapor() {}

    public Rapor(String raporAdi, String raporTipi, String format, Date baslangicTarihi, Date bitisTarihi) {
        this.raporAdi = raporAdi;
        this.raporTipi = raporTipi;
        this.format = format;
        this.baslangicTarihi = baslangicTarihi;
        this.bitisTarihi = bitisTarihi;
        this.durum = "OLUSTURULUYOR";
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
    public Kullanici getOlusturanKullanici() { return olusturanKullanici; }
    public void setOlusturanKullanici(Kullanici olusturanKullanici) { this.olusturanKullanici = olusturanKullanici; }
}
