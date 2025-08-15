package com.hospital.model;

public class Birim {
    private int birimId;
    private String birimAdi;
    private boolean aktif;

    // Constructors
    public Birim() {}

    public Birim(String birimAdi) {
        this.birimAdi = birimAdi;
        this.aktif = true;
    }

    // Getters and Setters
    public int getBirimId() { return birimId; }
    public void setBirimId(int birimId) { this.birimId = birimId; }

    public int getId() { return birimId; } // Eski kodlarla uyumluluk için
    public void setId(int id) { this.birimId = id; }

    public String getBirimAdi() { return birimAdi; }
    public void setBirimAdi(String birimAdi) { this.birimAdi = birimAdi; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
}