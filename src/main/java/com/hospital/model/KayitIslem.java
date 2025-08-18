package com.hospital.model;

import java.time.LocalDateTime;

public class KayitIslem {
    private int id;
    private LocalDateTime kayitZamani;
    private String hemsireKategori; // t1, t2, t3
    private int islemId;
    private int birimId;
    private int gercekSure; // Dakika cinsinden gerçek süre
    private String hastaDurumu; // kritik, normal
    private String notlar;
    private LocalDateTime createdAt;

    // Navigation properties
    private Birim birim;
    private Islem islem;

    // Constructors
    public KayitIslem() {}

    public KayitIslem(String hemsireKategori, int islemId, int birimId, int gercekSure) {
        this.kayitZamani = LocalDateTime.now();
        this.hemsireKategori = hemsireKategori;
        this.islemId = islemId;
        this.birimId = birimId;
        this.gercekSure = gercekSure;
        this.hastaDurumu = "normal";
    }

    public KayitIslem(String hemsireKategori, int islemId, int birimId, int gercekSure, String hastaDurumu) {
        this(hemsireKategori, islemId, birimId, gercekSure);
        this.hastaDurumu = hastaDurumu;
    }

    // Hemşire kategorisini enum benzeri kontrol eden method
    public boolean isValidKategori() {
        return hemsireKategori != null &&
                (hemsireKategori.equals("t1") ||
                        hemsireKategori.equals("t2") ||
                        hemsireKategori.equals("t3"));
    }

    // Hasta durumunu kontrol eden method
    public boolean isKritik() {
        return "kritik".equalsIgnoreCase(hastaDurumu);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getKayitZamani() { return kayitZamani; }
    public void setKayitZamani(LocalDateTime kayitZamani) { this.kayitZamani = kayitZamani; }

    public String getHemsireKategori() { return hemsireKategori; }
    public void setHemsireKategori(String hemsireKategori) { this.hemsireKategori = hemsireKategori; }

    public int getIslemId() { return islemId; }
    public void setIslemId(int islemId) { this.islemId = islemId; }

    public int getBirimId() { return birimId; }
    public void setBirimId(int birimId) { this.birimId = birimId; }

    public int getGercekSure() { return gercekSure; }
    public void setGercekSure(int gercekSure) { this.gercekSure = gercekSure; }

    public String getHastaDurumu() { return hastaDurumu; }
    public void setHastaDurumu(String hastaDurumu) { this.hastaDurumu = hastaDurumu; }

    // `hemsireId` ile ilgili satırlar kaldırıldı
    // public Integer getHemsireId() { return hemsireId; }
    // public void setHemsireId(Integer hemsireId) { this.hemsireId = hemsireId; }

    public String getNotlar() { return notlar; }
    public void setNotlar(String notlar) { this.notlar = notlar; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Birim getBirim() { return birim; }
    public void setBirim(Birim birim) { this.birim = birim; }

    public Islem getIslem() { return islem; }
    public void setIslem(Islem islem) { this.islem = islem; }

    @Override
    public String toString() {
        return String.format("KayitIslem{id=%d, kategori='%s', islemId=%d, birimId=%d, sure=%d, durum='%s'}",
                id, hemsireKategori, islemId, birimId, gercekSure, hastaDurumu);
    }
}