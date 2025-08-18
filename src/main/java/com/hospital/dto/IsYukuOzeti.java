package com.hospital.dto;

public class IsYukuOzeti {
    private String birimAdi;
    private String hemsireKategori;
    private int toplamIslem;
    private int toplamSureDk;
    private double toplamSureSaat;
    private double ortalamaIslemSure;

    // Constructors
    public IsYukuOzeti() {}

    public IsYukuOzeti(String birimAdi, String hemsireKategori, int toplamIslem, int toplamSureDk) {
        this.birimAdi = birimAdi;
        this.hemsireKategori = hemsireKategori;
        this.toplamIslem = toplamIslem;
        this.toplamSureDk = toplamSureDk;
        this.toplamSureSaat = toplamSureDk / 60.0;
        this.ortalamaIslemSure = toplamIslem > 0 ? (double) toplamSureDk / toplamIslem : 0;
    }

    // Getters and Setters
    public String getBirimAdi() { return birimAdi; }
    public void setBirimAdi(String birimAdi) { this.birimAdi = birimAdi; }

    public String getHemsireKategori() { return hemsireKategori; }
    public void setHemsireKategori(String hemsireKategori) { this.hemsireKategori = hemsireKategori; }

    public int getToplamIslem() { return toplamIslem; }
    public void setToplamIslem(int toplamIslem) {
        this.toplamIslem = toplamIslem;
        updateCalculations();
    }

    public int getToplamSureDk() { return toplamSureDk; }
    public void setToplamSureDk(int toplamSureDk) {
        this.toplamSureDk = toplamSureDk;
        updateCalculations();
    }

    public double getToplamSureSaat() { return toplamSureSaat; }
    public double getOrtalamaIslemSure() { return ortalamaIslemSure; }

    private void updateCalculations() {
        this.toplamSureSaat = toplamSureDk / 60.0;
        this.ortalamaIslemSure = toplamIslem > 0 ? (double) toplamSureDk / toplamIslem : 0;
    }

    @Override
    public String toString() {
        return String.format("IsYukuOzeti{birim='%s', kategori='%s', islem=%d, sure=%.2fsa}",
                birimAdi, hemsireKategori, toplamIslem, toplamSureSaat);
    }
}
