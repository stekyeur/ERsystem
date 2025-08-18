package com.hospital.model;

import java.time.LocalDateTime;

public class Birim {
    private int id;
    private String birimAdi;
    private String aciklama;
    private int kapasite;
    private boolean aktif;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Birim() {}

    public Birim(String birimAdi, String aciklama, int kapasite) {
        this.birimAdi = birimAdi;
        this.aciklama = aciklama;
        this.kapasite = kapasite;
        this.aktif = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBirimAdi() { return birimAdi; }
    public void setBirimAdi(String birimAdi) { this.birimAdi = birimAdi; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public int getKapasite() { return kapasite; }
    public void setKapasite(int kapasite) { this.kapasite = kapasite; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return String.format("Birim{id=%d, birimAdi='%s', kapasite=%d}",
                id, birimAdi, kapasite);
    }
}