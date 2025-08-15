package com.hospital.model;

public class IslemTuru {
    private int id;
    private String islemAdi;
    private String kategori;
    private int ortalamaSure;
    private boolean aktif;

    // Constructors
    public IslemTuru() {}

    public IslemTuru(String islemAdi, String kategori, int ortalamaSure) {
        this.islemAdi = islemAdi;
        this.kategori = kategori;
        this.ortalamaSure = ortalamaSure;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIslemAdi() { return islemAdi; }
    public void setIslemAdi(String islemAdi) { this.islemAdi = islemAdi; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getIslemTipi() { return kategori; } // Eski kodlarla uyumluluk için
    public void setIslemTipi(String islemTipi) { this.kategori = islemTipi; }

    public int getOrtalamaSure() { return ortalamaSure; }
    public void setOrtalamaSure(int ortalamaSure) { this.ortalamaSure = ortalamaSure; }

    public int getStandartSureDk() { return ortalamaSure; } // Eski kodlarla uyumluluk için
    public void setStandartSureDk(int standartSureDk) { this.ortalamaSure = standartSureDk; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}