package com.hospital.model;

import java.sql.Timestamp;
import java.util.Date;

public class Islem {
    private int id;
    private int kayitIndeksi;
    private Date tarih;
    private int hemsireId;
    private int islemId;
    private int birimId;
    private int sureDakika;
    private String islemTipi; // ACİL, RUTİN vb.
    private String kritiklik; // YÜKSEK, ORTA, DÜŞÜK vb.
    private String notlar;
    private Timestamp kayitTarihi;
    private int kaydedenKullaniciId;

    // İlişkili objeler
    private Hemsire hemsire;
    private IslemTuru islemTuru;
    private Birim birim;
    private Kullanici kaydedenKullanici;

    public Islem() {}

    public Islem(Date tarih, int hemsireId, int islemId, int birimId, int sureDakika,
                 String islemTipi, String kritiklik) {
        this.tarih = tarih;
        this.hemsireId = hemsireId;
        this.islemId = islemId;
        this.birimId = birimId;
        this.sureDakika = sureDakika;
        this.islemTipi = islemTipi;
        this.kritiklik = kritiklik;
    }

    // Getters ve Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getKayitIndeksi() { return kayitIndeksi; }
    public void setKayitIndeksi(int kayitIndeksi) { this.kayitIndeksi = kayitIndeksi; }
    public Date getTarih() { return tarih; }
    public void setTarih(Date tarih) { this.tarih = tarih; }
    public int getHemsireId() { return hemsireId; }
    public void setHemsireId(int hemsireId) { this.hemsireId = hemsireId; }
    public int getIslemId() { return islemId; }
    public void setIslemId(int islemId) { this.islemId = islemId; }
    public int getBirimId() { return birimId; }
    public void setBirimId(int birimId) { this.birimId = birimId; }
    public int getSureDakika() { return sureDakika; }
    public void setSureDakika(int sureDakika) { this.sureDakika = sureDakika; }
    public String getIslemTipi() { return islemTipi; }
    public void setIslemTipi(String islemTipi) { this.islemTipi = islemTipi; }
    public String getKritiklik() { return kritiklik; }
    public void setKritiklik(String kritiklik) { this.kritiklik = kritiklik; }
    public String getNotlar() { return notlar; }
    public void setNotlar(String notlar) { this.notlar = notlar; }
    public Timestamp getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(Timestamp kayitTarihi) { this.kayitTarihi = kayitTarihi; }
    public int getKaydedenKullaniciId() { return kaydedenKullaniciId; }
    public void setKaydedenKullaniciId(int kaydedenKullaniciId) { this.kaydedenKullaniciId = kaydedenKullaniciId; }

    public Hemsire getHemsire() { return hemsire; }
    public void setHemsire(Hemsire hemsire) { this.hemsire = hemsire; }
    public IslemTuru getIslemTuru() { return islemTuru; }
    public void setIslemTuru(IslemTuru islemTuru) { this.islemTuru = islemTuru; }
    public Birim getBirim() { return birim; }
    public void setBirim(Birim birim) { this.birim = birim; }
    public Kullanici getKaydedenKullanici() { return kaydedenKullanici; }
    public void setKaydedenKullanici(Kullanici kaydedenKullanici) { this.kaydedenKullanici = kaydedenKullanici; }
}
